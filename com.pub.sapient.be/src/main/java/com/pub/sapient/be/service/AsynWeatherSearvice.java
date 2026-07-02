package com.pub.sapient.be.service;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.pub.sapient.be.dto.WeatherResponseDto;

@Service
public class AsynWeatherSearvice {

	private static final Logger log = LoggerFactory.getLogger(WeatherClientService.class);

	private final WeatherService weatherService;

	public AsynWeatherSearvice(WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	@Async("weatherExecutor")
	public CompletableFuture<WeatherResponseDto> getWeatherAsync(String city) {

		log.info("Started {} on {}", city, Thread.currentThread().getName());

		WeatherResponseDto dto = weatherService.get3DayForecast(city, true);

		log.info("Completed {} on {}", city, Thread.currentThread().getName());

		return CompletableFuture.completedFuture(dto);
	}
}
