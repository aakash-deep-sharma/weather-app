package com.pub.sapient.be.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CityNotFoundExceptionTest {

	@Test
	void testExceptionConstructorAndGetter() {
		// Arrange
		String city = "Ajmer1";
		String message = "City not found in database.";

		// Act
		CityNotFoundException exception = new CityNotFoundException(city, message);

		// Assert
		assertEquals(message, exception.getMessage());
		assertEquals(city, exception.getCity());
	}
}