package com.pub.sapient.be.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class WeatherResponseDtoTest {

	@Test
	void testEqualityAndHashCode() {
		// Arrange
		List<WeatherForecastDto> forecasts = List.of(new WeatherForecastDto("2026-07-02", 30.0, 20.0, List.of()));
		WeatherResponseDto dto1 = new WeatherResponseDto("Ajmer", "LIVE_DATA", forecasts);
		WeatherResponseDto dto2 = new WeatherResponseDto("Ajmer", "LIVE_DATA", forecasts);
		WeatherResponseDto dto3 = new WeatherResponseDto("Jaipur", "LIVE_DATA", forecasts);

		// Assert equality
		assertEquals(dto1, dto2, "Objects with same data should be equal");
		assertEquals(dto1.hashCode(), dto2.hashCode(), "Equal objects must have same hashCode");

		// Assert inequality
		assertNotEquals(dto1, dto3, "Objects with different cities should not be equal");
	}

	@Test
	void testHateoasLinkFunctionality() {
		// Arrange
		WeatherResponseDto dto = new WeatherResponseDto("Ajmer", "LIVE_DATA", List.of());

		// Act: Add a HATEOAS link
		dto.add(org.springframework.hateoas.Link.of("http://api.example.com/weather/Ajmer", "self"));

		// Assert: Verify link exists
		assertTrue(dto.getLink("self").isPresent());
		assertEquals("http://api.example.com/weather/Ajmer", dto.getLink("self").get().getHref());
	}
}