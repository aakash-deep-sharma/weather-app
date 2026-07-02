package com.pub.sapient.be.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pub.sapient.be.dto.WeatherResponseDto;
import com.pub.sapient.be.service.AsynWeatherSearvice;
import com.pub.sapient.be.service.WeatherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
@Tag(name = "Weather API Engine")
public class WeatherController {

	private final WeatherService weatherService;
	private final AsynWeatherSearvice asynWeatherSearvice;

	public WeatherController(WeatherService weatherService, AsynWeatherSearvice asynWeatherSearvice) {
		this.weatherService = weatherService;
		this.asynWeatherSearvice = asynWeatherSearvice;
	}

	@GetMapping
	@Operation(summary = "Get 3-day weather summary predictions for an input city.")
	public ResponseEntity<WeatherResponseDto> get3DayForecast(@RequestParam String city,
			@RequestParam(defaultValue = "false") boolean offlineMode) {

		WeatherResponseDto responseDto = weatherService.get3DayForecast(city, offlineMode);

		// Standard link insertion via inherited .add() method
		responseDto.add(linkTo(methodOn(WeatherController.class).get3DayForecast(city, offlineMode)).withSelfRel());

		// Dynamic state link conditional on response content
		if (responseDto.getForecasts() != null && responseDto.getForecasts().stream()
				.anyMatch(f -> f.getAlerts() != null && !f.getAlerts().isEmpty())) {

			responseDto.add(linkTo(methodOn(WeatherController.class).get3DayForecast(city, true))
					.withRel("emergency_offline_view"));
		}

		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("multiple-cities")
	@Operation(summary = "Get weather summary predictions for input cities (comma separated list).")
	public ResponseEntity<CollectionModel<WeatherResponseDto>> getCitiesForecast(@RequestParam String cities) {

		List<CompletableFuture<WeatherResponseDto>> futures = Arrays.stream(cities.split(",")).map(String::trim)
				.map(asynWeatherSearvice::getWeatherAsync).toList();

		List<WeatherResponseDto> responses = futures.stream().map(CompletableFuture::join).toList();

		responses.forEach(response -> response.add(
				linkTo(methodOn(WeatherController.class).get3DayForecast(response.getCity(), false)).withSelfRel()));

		CollectionModel<WeatherResponseDto> collectionModel = CollectionModel.of(responses);

		return ResponseEntity.ok(collectionModel);
	}

}