package com.pub.sapient.be.rule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pub.sapient.be.dto.WeatherAlertDto;
import com.pub.sapient.be.model.OpenWeatherPayload.ForecastData;
import com.pub.sapient.be.model.OpenWeatherPayload.WindData;

class WindRuleTest {

	private WindRule windRule;

	@BeforeEach
	void setUp() {
		windRule = new WindRule();
	}

	@Test
	void evaluate_ShouldReturnAlert_WhenWindSpeedExceeds10() {
		// Arrange: Speed 15.0
		ForecastData data = createForecastData(15.0);

		// Act
		Optional<WeatherAlertDto> result = windRule.evaluate(data);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("WIND_RULE", result.get().message());
	}

	@Test
	void evaluate_ShouldReturnEmpty_WhenWindSpeedIsBelowThreshold() {
		// Arrange: Speed 5.0
		ForecastData data = createForecastData(5.0);

		// Act
		Optional<WeatherAlertDto> result = windRule.evaluate(data);

		// Assert
		assertTrue(result.isEmpty());
	}

	@Test
	void evaluate_ShouldReturnEmpty_WhenWindDataIsNull() {
		// Arrange
		ForecastData data = new ForecastData();
		data.setWind(null);

		// Act
		Optional<WeatherAlertDto> result = windRule.evaluate(data);

		// Assert
		assertTrue(result.isEmpty());
	}

	private ForecastData createForecastData(double speed) {
		ForecastData data = new ForecastData();
		WindData wind = new WindData();
		wind.setSpeed(speed);
		data.setWind(wind);
		return data;
	}
}