package com.pub.sapient.be.rule;

import java.util.Optional;

import com.pub.sapient.be.dto.WeatherAlertDto;
import com.pub.sapient.be.model.OpenWeatherPayload.ForecastData;

public interface WeatherRule {
	Optional<WeatherAlertDto> evaluate(ForecastData data);
}
