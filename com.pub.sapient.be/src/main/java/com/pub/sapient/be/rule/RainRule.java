package com.pub.sapient.be.rule;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.pub.sapient.be.dto.WeatherAlertDto;
import com.pub.sapient.be.model.OpenWeatherPayload.ForecastData;

@Component
public class RainRule implements WeatherRule {
	@Override
	public Optional<WeatherAlertDto> evaluate(ForecastData data) {
		boolean isRainy = data.getWeather().stream().anyMatch(w -> w.getMain().equalsIgnoreCase("Rain"));
		return isRainy ? Optional.of(new WeatherAlertDto("RAIN_RULE")) : Optional.empty();
	}
}
