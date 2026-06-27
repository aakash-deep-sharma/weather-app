package com.pub.sapient.be.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pub.sapient.be.dto.WeatherResponseDto;
import com.pub.sapient.be.service.WeatherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
@Tag(name = "Weather API Engine", description = "Retrieves high/low forecasts along with dynamic environmental logic triggers.")
public class WeatherController {

	private final WeatherService weatherService;

	public WeatherController(WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	@GetMapping
	@Operation(summary = "Get 3-day weather summary predictions for an input city.")
	public ResponseEntity<WeatherResponseDto> get3DayForecast(@RequestParam String city,
			@RequestParam(defaultValue = "false") boolean offlineMode) {

		return ResponseEntity.ok(weatherService.get3DayForecast(city, offlineMode));

	}
}
