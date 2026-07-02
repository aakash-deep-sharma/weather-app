package com.pub.sapient.be.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class WeatherForecastDtoTest {

	@Test
	void testWeatherForecastDtoConstructorAndGetters() {
		// Arrange
		String date = "2026-07-02";
		double maxTemp = 35.5;
		double minTemp = 25.0;
		List<String> alerts = List.of("HEAT_ALERT", "WIND_ALERT");

		// Act
		WeatherForecastDto dto = new WeatherForecastDto(date, maxTemp, minTemp, alerts);

		// Assert
		assertEquals(date, dto.getDate());
		assertEquals(maxTemp, dto.getMaxTemp());
		assertEquals(minTemp, dto.getMinTemp());
		assertEquals(alerts, dto.getAlerts());
		assertEquals(2, dto.getAlerts().size());
	}

	@Test
	void testSetters() {
		// Arrange
		WeatherForecastDto dto = new WeatherForecastDto("2026-07-02", 0.0, 0.0, List.of());

		// Act
		dto.setMaxTemp(40.0);
		dto.setMinTemp(20.0);
		dto.setAlerts(List.of("NEW_ALERT"));

		// Assert
		assertEquals(40.0, dto.getMaxTemp());
		assertEquals(20.0, dto.getMinTemp());
		assertEquals("NEW_ALERT", dto.getAlerts().get(0));
	}
}