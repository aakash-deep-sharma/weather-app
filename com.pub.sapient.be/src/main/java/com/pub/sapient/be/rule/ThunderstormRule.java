package com.pub.sapient.be.rule;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.pub.sapient.be.dto.WeatherAlertDto;
import com.pub.sapient.be.model.OpenWeatherPayload.ForecastData;

@Component
public class ThunderstormRule implements WeatherRule {
	@Override
	public Optional<WeatherAlertDto> evaluate(ForecastData data) {
		boolean isStorm = data.getWeather().stream().anyMatch(w -> w.getMain().equalsIgnoreCase("Thunderstorm"));
		return isStorm ? Optional.of(new WeatherAlertDto("THUNDERSTROM_RULE")) : Optional.empty();
	}
}
