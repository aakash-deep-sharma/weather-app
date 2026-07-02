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

class ThunderstormRuleTest {

	private ThunderstormRule thunderstormRule;

	@BeforeEach
	void setUp() {
		thunderstormRule = new ThunderstormRule();
	}

	@Test
	void evaluate_ShouldReturnAlert_WhenThunderstormIsPresent() {
		// Arrange
		ForecastData data = createForecastData("Thunderstorm");

		// Act
		Optional<WeatherAlertDto> result = thunderstormRule.evaluate(data);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("THUNDERSTROM_RULE", result.get().message());
	}

	@Test
	void evaluate_ShouldReturnEmpty_WhenNoThunderstorm() {
		// Arrange
		ForecastData data = createForecastData("Clouds");

		// Act
		Optional<WeatherAlertDto> result = thunderstormRule.evaluate(data);

		// Assert
		assertTrue(result.isEmpty());
	}

	private ForecastData createForecastData(String conditionMain) {
		ForecastData data = new ForecastData();
		WeatherCondition condition = new WeatherCondition();
		condition.setMain(conditionMain);
		data.setWeather(List.of(condition));
		return data;
	}
}