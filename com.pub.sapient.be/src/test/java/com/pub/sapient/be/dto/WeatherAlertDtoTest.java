package com.pub.sapient.be.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class WeatherAlertDtoTest {

	@Test
	void testRecordPropertiesAndEquality() {
		// Arrange
		String message1 = "Heat Alert";
		String message2 = "Heat Alert";

		// Act
		WeatherAlertDto dto1 = new WeatherAlertDto(message1);
		WeatherAlertDto dto2 = new WeatherAlertDto(message2);

		// Assert
		assertEquals(message1, dto1.message());
		assertEquals(dto1, dto2); // Verifies equals() implementation
		assertEquals(dto1.hashCode(), dto2.hashCode()); // Verifies hashCode() implementation
	}
}