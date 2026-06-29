package com.pub.sapient.be.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import com.pub.sapient.be.client.OpenWeatherClient;
import com.pub.sapient.be.dto.WeatherForecastDto;
import com.pub.sapient.be.dto.WeatherResponseDto;
import com.pub.sapient.be.model.OpenWeatherPayload;
import com.pub.sapient.be.model.OpenWeatherPayload.ForecastData;
import com.pub.sapient.be.rule.WeatherRule;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class WeatherService {

	private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

	@Value("${weather.api.key}")
	private String API_KEY;
	@Value("${weather.api.endpoints.result-count}")
	private String apiResultCount;
	private final List<WeatherRule> weatherRules;
	private final OpenWeatherClient openWeatherClient;
	private final CacheManager cacheManager;

	public WeatherService(CacheManager cacheManager, OpenWeatherClient openWeatherClient,
			List<WeatherRule> weatherRules) {
		this.openWeatherClient = openWeatherClient;
		this.weatherRules = weatherRules;
		this.cacheManager = cacheManager;
	}

	@Retry(name = "openWeatherRetry")
	@CircuitBreaker(name = "openWeatherApi", fallbackMethod = "fallbackWeatherData")
	@CachePut(value = "weatherCache", key = "#city.toLowerCase()", unless = "#result.notes.contains('Offline')")
	public WeatherResponseDto get3DayForecast(String city, boolean offlineMode) {
		log.info("Weather service called");
		if (offlineMode) {
			log.warn("Running offline mode.");
			throw new RuntimeException("Force offline mode active");
		}

		OpenWeatherPayload payload = openWeatherClient.fetch3DayForecast(city, API_KEY,
				Integer.valueOf(this.apiResultCount));
		if (payload == null || payload.getList() == null) {
			log.warn("Open weather return empty payload.");
			throw new RuntimeException("Empty response from Weather API");
		}

		return processPayload(city, payload, "LIVE_DATA");
	}

	// Fallback Logic mapping clean custom business entities in offline scenarios
	public WeatherResponseDto fallbackWeatherData(String city, boolean offlineMode, Throwable t) {

		log.warn("Circuit Breaker / Retry triggered fallback for city: {} due to: {}", city, t.getMessage());

		WeatherResponseDto errorResponse = new WeatherResponseDto();

		// Catch the specific 404 City Not Found exception
		if (t instanceof feign.FeignException.NotFound || t.getCause() instanceof feign.FeignException.NotFound) {
			errorResponse.setDataCode("CITY_NOT_FOUND");
			errorResponse.setCity(city);
			errorResponse.setNotes("CITY_NOT_FOUND");
			return errorResponse;
		}

		Cache cache = cacheManager.getCache("weatherCache");

		if (cache != null) {
			WeatherResponseDto cachedData = cache.get(city.toLowerCase(), WeatherResponseDto.class);
			if (cachedData != null) {

				log.warn("Circuit Breaker / Retry triggered fallback for city: {}. Using cached data", city);
				// Update the user notes so they know they are viewing a cached snapshot
				cachedData.setNotes("CACHE_DATA");
				cachedData.setDataCode("CACHE_DATA_FOUND");
				return cachedData;
			}
		}

		// Hard fallback if there is zero historical cache available for this specific
		// city query
		log.warn("Circuit Breaker / Retry triggered fallback for city: {}. No cached data", city);

		return new WeatherResponseDto(city, "WEATHER_SERVICE_DOWN", List.of(), "NO_DATA");
	}

	private WeatherResponseDto processPayload(String city, OpenWeatherPayload payload, String sourceNote) {
		Map<String, List<ForecastData>> groupedByDay = payload.getList().stream()
				.collect(Collectors.groupingBy(data -> data.getDtTxt().split(" ")[0]));

		List<WeatherForecastDto> calculatedForecasts = groupedByDay.entrySet().stream().limit(3).map(entry -> {
			String date = entry.getKey();
			List<ForecastData> dayData = entry.getValue();

			double maxTemp = dayData.stream().mapToDouble(d -> d.getMain().getTempMax() - 273.15).max().orElse(0.0);
			double minTemp = dayData.stream().mapToDouble(d -> d.getMain().getMinTemp() - 273.15).min().orElse(0.0);

			// Execute rules injection dynamically
			List<String> alerts = dayData.stream().flatMap(d -> weatherRules.stream().map(rule -> rule.evaluate(d)))
					.filter(Optional::isPresent).map(opt -> opt.get().message()).distinct()
					.collect(Collectors.toList());

			return new WeatherForecastDto(date, Math.round(maxTemp * 100.0) / 100.0,
					Math.round(minTemp * 100.0) / 100.0, alerts);
		}).sorted(Comparator.comparing(WeatherForecastDto::getDate)).collect(Collectors.toList());

		return new WeatherResponseDto(city, sourceNote, calculatedForecasts, "DATA_FOUND");
	}
}
