package com.pub.sapient.be.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pub.sapient.be.client.OpenWeatherClient;
import com.pub.sapient.be.model.OpenWeatherPayload;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class WeatherClientService {

	public final String CACHE_DATA = "CACHE_DATA";
	public final String CITY_NOT_FOUND = "CITY_NOT_FOUND";
	public final String LIVE_DATA = "LIVE_DATA";

	private static final Logger log = LoggerFactory.getLogger(WeatherClientService.class);

	@Value("${weather.api.key}")
	private String apiKey;
	@Value("${weather.api.endpoints.result-count}")
	private String apiResultCount;

	private final OpenWeatherClient openWeatherClient;
	private final WeatherCacheService cacheService;

	public WeatherClientService(OpenWeatherClient openWeatherClient, WeatherCacheService cacheService) {
		this.openWeatherClient = openWeatherClient;
		this.cacheService = cacheService;
	}

	@Retry(name = "openWeatherRetry")
	@CircuitBreaker(name = "openWeatherApi", fallbackMethod = "fallbackWeatherData")
	public OpenWeatherPayload fetchForecast(String city, boolean offlineMode) {
		if (offlineMode) {
			log.warn("Running offline mode.");
			throw new RuntimeException("Force offline mode active.");
		}
		OpenWeatherPayload response = openWeatherClient.fetch3DayForecast(city, apiKey,
				Integer.valueOf(apiResultCount));
		response.setDataCode(LIVE_DATA);
		cacheService.put(city, response);
		return response;
	}

	public OpenWeatherPayload fallbackWeatherData(String city, boolean offlineMode, Throwable t) {

		log.warn("Circuit Breaker / Retry triggered fallback for city: {} due to: {}", city, t.getMessage());
		if (t instanceof feign.FeignException.NotFound || t.getCause() instanceof feign.FeignException.NotFound) {
			OpenWeatherPayload errorResponse = new OpenWeatherPayload();
			errorResponse.setDataCode(CITY_NOT_FOUND);
			errorResponse.setCity(city);
			return errorResponse;
		}

		OpenWeatherPayload cachedPayload = this.cacheService.get(city);
		if (cachedPayload != null) {
			log.warn("Circuit Breaker / Retry triggered fallback for city: {}. Using cached data", city);
			cachedPayload.setDataCode(CACHE_DATA);
			return cachedPayload;
		}

		log.warn("Circuit Breaker / Retry triggered fallback for city: {}. No cached data", city);
		OpenWeatherPayload emptyResponse = new OpenWeatherPayload();
		emptyResponse.setCity(city);
		emptyResponse.setDataCode(CITY_NOT_FOUND);
		return emptyResponse;
	}
}
