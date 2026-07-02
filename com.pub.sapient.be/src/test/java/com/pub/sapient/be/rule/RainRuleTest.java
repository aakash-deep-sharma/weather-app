package com.pub.sapient.be.rule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pub.sapient.be.dto.WeatherAlertDto;
import com.pub.sapient.be.model.OpenWeatherPayload.ForecastData;
import com.pub.sapient.be.model.OpenWeatherPayload.WeatherCondition;

class RainRuleTest {

	private RainRule rainRule;

	@BeforeEach
	void setUp() {
		rainRule = new RainRule();
	}

	@Test
	void evaluate_ShouldReturnAlert_WhenRainIsPresent() {
		// Arrange
		ForecastData data = new ForecastData();
		WeatherCondition condition = new WeatherCondition();
		condition.setMain("Rain");
		data.setWeather(List.of(condition));

		// Act
		Optional<WeatherAlertDto> result = rainRule.evaluate(data);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("RAIN_RULE", result.get().message());
	}

	@Test
	void evaluate_ShouldReturnEmpty_WhenNoRainPresent() {
		// Arrange
		ForecastData data = new ForecastData();
		WeatherCondition condition = new WeatherCondition();
		condition.setMain("Clear");
		data.setWeather(List.of(condition));

		// Act
		Optional<WeatherAlertDto> result = rainRule.evaluate(data);

		// Assert
		assertTrue(result.isEmpty());
	}
}