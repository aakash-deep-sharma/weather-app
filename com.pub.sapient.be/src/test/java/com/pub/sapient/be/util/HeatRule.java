package com.pub.sapient.be.util;

import java.util.Optional;

import com.pub.sapient.be.dto.WeatherAlertDto;
import com.pub.sapient.be.model.OpenWeatherPayload.ForecastData;
import com.pub.sapient.be.rule.WeatherRule;

public class HeatRule implements WeatherRule {
	@Override
	public Optional<WeatherAlertDto> evaluate(ForecastData data) {
		double celsius = data.getMain().getTempMax() - 273.15;
		if (celsius > 35.0) {
			return Optional.of(new WeatherAlertDto("Heatwave Alert: High temperature detected."));
		}
		return Optional.empty();
	}
}
