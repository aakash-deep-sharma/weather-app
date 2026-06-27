package com.pub.sapient.be.rule;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.pub.sapient.be.dto.WeatherAlertDto;
import com.pub.sapient.be.model.OpenWeatherPayload.ForecastData;

@Component
public class WindRule implements WeatherRule {
	@Override
	public Optional<WeatherAlertDto> evaluate(ForecastData data) {
		return (data.getWind() != null && data.getWind().getSpeed() > 10.0)
				? Optional.of(new WeatherAlertDto("WIND_RULE"))
				: Optional.empty();
	}
}
