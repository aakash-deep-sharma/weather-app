package com.pub.sapient.be.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pub.sapient.be.dto.WeatherResponseDto;
import com.pub.sapient.be.exceptions.CityNotFoundException;
import com.pub.sapient.be.exceptions.RateLimitException;
import com.pub.sapient.be.model.OpenWeatherPayload;
import com.pub.sapient.be.service.WeatherCacheService;
import com.pub.sapient.be.util.Utility;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private final WeatherCacheService cacheService;
	private final Utility utility;

	public GlobalExceptionHandler(WeatherCacheService cacheService, Utility utility) {
		this.cacheService = cacheService;
		this.utility = utility;
	}

	@ExceptionHandler(RateLimitException.class)
	public ResponseEntity<WeatherResponseDto> handleRateLimitException(RateLimitException ex) {
		OpenWeatherPayload cachedPayload = this.cacheService.get(ex.getCity());
		if (cachedPayload == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new WeatherResponseDto(ex.getCity(), utility.CITY_NOT_FOUND, List.of()));
		}
		cachedPayload.setDataCode(utility.CACHE_DATA);
		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
				.body(utility.processPayload(ex.getCity(), cachedPayload, cachedPayload.getDataCode()));
	}

	@ExceptionHandler(CityNotFoundException.class)
	public ResponseEntity<WeatherResponseDto> handleCityNotFoundException(CityNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new WeatherResponseDto(ex.getCity(), utility.CITY_NOT_FOUND, List.of()));
	}

}
