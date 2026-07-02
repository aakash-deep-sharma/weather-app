package com.pub.sapient.be.util;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.pub.sapient.be.dto.WeatherForecastDto;
import com.pub.sapient.be.dto.WeatherResponseDto;
import com.pub.sapient.be.model.OpenWeatherPayload;
import com.pub.sapient.be.model.OpenWeatherPayload.ForecastData;
import com.pub.sapient.be.rule.WeatherRule;

@Component
public class Utility {

	public final String CACHE_DATA = "CACHE_DATA";
	public final String CITY_NOT_FOUND = "CITY_NOT_FOUND";
	public final String LIVE_DATA = "LIVE_DATA";

	private final List<WeatherRule> weatherRules;

	public Utility(List<WeatherRule> weatherRules) {
		this.weatherRules = weatherRules;
	}

	public WeatherResponseDto processPayload(String city, OpenWeatherPayload payload, String sourceNote) {

		if (CITY_NOT_FOUND.equals(sourceNote)) {
			return new WeatherResponseDto(city, sourceNote, List.of());
		}

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

		return new WeatherResponseDto(city, sourceNote, calculatedForecasts);
	}
}
