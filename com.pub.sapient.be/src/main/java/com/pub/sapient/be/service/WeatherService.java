package com.pub.sapient.be.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pub.sapient.be.dto.WeatherResponseDto;
import com.pub.sapient.be.exceptions.RateLimitException;
import com.pub.sapient.be.model.OpenWeatherPayload;
import com.pub.sapient.be.rule.WeatherRule;
import com.pub.sapient.be.util.Utility;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@Service
public class WeatherService {

	private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

	private final WeatherClientService clientService;
	private final WeatherCacheService cacheService;
	private final Utility utility;

	public WeatherService(WeatherClientService clientService, WeatherCacheService cacheService, Utility utility,
			List<WeatherRule> weatherRules) {
		this.clientService = clientService;
		this.cacheService = cacheService;
		this.utility = utility;
	}

	@RateLimiter(name = "openWeatherRateLimiter", fallbackMethod = "rateLimitFallback")
	public WeatherResponseDto get3DayForecast(String city, boolean offlineMode) {
		if (offlineMode) {
			OpenWeatherPayload cachedPayload = this.cacheService.get(city);
			if (cachedPayload != null) {
				log.warn("Offline mode active returning cached data for city: {}", city);
				cachedPayload.setDataCode(utility.CACHE_DATA);
				return utility.processPayload(city, cachedPayload, cachedPayload.getDataCode());
			}
		}

		OpenWeatherPayload response = this.clientService.fetchForecast(city);
		this.cacheService.put(city, response);
		return utility.processPayload(city, response, response.getDataCode());
	}

	public WeatherResponseDto rateLimitFallback(String city, boolean offlineMode, RequestNotPermitted ex) {
		throw new RateLimitException(city, "Rate limit exceeded.");
	}

}
