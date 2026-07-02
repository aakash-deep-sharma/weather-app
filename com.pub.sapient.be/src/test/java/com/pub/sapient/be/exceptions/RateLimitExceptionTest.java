package com.pub.sapient.be.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RateLimitExceptionTest {

	@Test
	void testRateLimitExceptionStoresFieldsCorrectly() {
		// Arrange
		String city = "Ajmer";
		String message = "Rate limit exceeded.";

		// Act
		RateLimitException exception = new RateLimitException(city, message);

		// Assert
		assertEquals(message, exception.getMessage());
		assertEquals(city, exception.getCity());
	}
}