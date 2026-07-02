package com.pub.sapient.be.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pub.sapient.be.client.OpenWeatherClient;
import com.pub.sapient.be.exceptions.CityNotFoundException;
import com.pub.sapient.be.model.OpenWeatherPayload;
import com.pub.sapient.be.util.Utility;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class WeatherClientService {

	private static final Logger log = LoggerFactory.getLogger(WeatherClientService.class);

	@Value("${weather.api.key}")
	private String apiKey;
	@Value("${weather.api.endpoints.result-count}")
	private String apiResultCount;

	private final OpenWeatherClient openWeatherClient;
	private final WeatherCacheService cacheService;
	private final Utility utility;

	public WeatherClientService(OpenWeatherClient openWeatherClient, WeatherCacheService cacheService,
			Utility utility) {
		this.openWeatherClient = openWeatherClient;
		this.cacheService = cacheService;
		this.utility = utility;
	}

	@CircuitBreaker(name = "openWeatherApi", fallbackMethod = "fallbackWeatherData")
	public OpenWeatherPayload fetchForecast(String city) {
		OpenWeatherPayload response = openWeatherClient.fetch3DayForecast(city, apiKey,
				Integer.valueOf(apiResultCount));
		response.setDataCode(utility.LIVE_DATA);
		return response;
	}

	public OpenWeatherPayload fallbackWeatherData(String city, Throwable t) {

		log.warn("Circuit Breaker / Retry triggered fallback for city: {} due to: {}", city, t.getMessage());
		if (t instanceof feign.FeignException.NotFound) {
			throw new CityNotFoundException(city, "City not found in open weather database.");
		}

		OpenWeatherPayload cachedPayload = this.cacheService.get(city);
		if (cachedPayload != null) {
			log.warn("Circuit Breaker fallback for city: {}. Using cached data", city);
			cachedPayload.setDataCode(utility.CACHE_DATA);
			return cachedPayload;
		}

		log.warn("Circuit Breaker fallback for city: {}. No cached data", city);
		throw new CityNotFoundException(city, "Running offline mode city not found in cache.");
	}
}
