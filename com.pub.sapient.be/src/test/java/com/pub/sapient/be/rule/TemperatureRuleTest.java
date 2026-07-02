package com.pub.sapient.be.rule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pub.sapient.be.model.OpenWeatherPayload.ForecastData;
import com.pub.sapient.be.model.OpenWeatherPayload.MainData;

class TemperatureRuleTest {

	private TemperatureRule temperatureRule;

	@BeforeEach
	void setUp() {
		temperatureRule = new TemperatureRule();
	}

	@Test
	void evaluate_ShouldReturnAlert_WhenTempAbove40Celsius() {
		// Arrange: 314.15K - 273.15 = 41.0C
		ForecastData data = createForecastData(314.15);

		// Act
		var result = temperatureRule.evaluate(data);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("TEMPERATURE_RULE", result.get().message());
	}

	@Test
	void evaluate_ShouldReturnEmpty_WhenTempBelow40Celsius() {
		// Arrange: 313.14K - 273.15 = 39.99C
		ForecastData data = createForecastData(313.14);

		// Act
		var result = temperatureRule.evaluate(data);

		// Assert
		assertTrue(result.isEmpty());
	}

	// Helper to keep tests clean
	private ForecastData createForecastData(double kelvin) {
		ForecastData data = new ForecastData();
		MainData main = new MainData();
		main.setTempMax(kelvin);
		data.setMain(main);
		return data;
	}
}