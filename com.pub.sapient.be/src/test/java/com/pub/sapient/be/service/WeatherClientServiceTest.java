package com.pub.sapient.be.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.pub.sapient.be.client.OpenWeatherClient;
import com.pub.sapient.be.exceptions.CityNotFoundException;
import com.pub.sapient.be.model.OpenWeatherPayload;
import com.pub.sapient.be.util.Utility;

@ExtendWith(MockitoExtension.class)
class WeatherClientServiceTest {

	@Mock
	private OpenWeatherClient openWeatherClient;
	@Mock
	private WeatherCacheService cacheService;
	@Mock
	private Utility utility;

	@InjectMocks
	private WeatherClientService weatherClientService;

	@BeforeEach
	void setUp() {
		// Inject values directly as they are private fields with @Value
		ReflectionTestUtils.setField(weatherClientService, "apiKey", "test-key");
		ReflectionTestUtils.setField(weatherClientService, "apiResultCount", "3");
	}

	@Test
	void fetchForecast_Success() {
		// Arrange
		String city = "Delhi";
		OpenWeatherPayload mockPayload = new OpenWeatherPayload();
		when(openWeatherClient.fetch3DayForecast(eq(city), anyString(), anyInt())).thenReturn(mockPayload);

		// Act
		OpenWeatherPayload result = weatherClientService.fetchForecast(city);

		// Assert
		// Use the actual constant from your Utility class instead of stubbing it
		assertEquals(utility.LIVE_DATA, result.getDataCode());
		verify(openWeatherClient).fetch3DayForecast(eq(city), eq("test-key"), eq(3));
	}

	@Test
	void fallbackWeatherData_NotFoundException_ThrowsCityNotFound() {
		// Arrange
		String city = "InvalidCity";
		feign.FeignException.NotFound exception = mock(feign.FeignException.NotFound.class);

		// Act & Assert
		assertThrows(CityNotFoundException.class, () -> weatherClientService.fallbackWeatherData(city, exception));
	}

	@Test
	void fallbackWeatherData_CacheAvailable_ReturnsCachedData() {
		// Arrange
		String city = "Delhi";
		OpenWeatherPayload cached = new OpenWeatherPayload();
		when(cacheService.get(city)).thenReturn(cached);

		// Act
		OpenWeatherPayload result = weatherClientService.fallbackWeatherData(city,
				new RuntimeException("Generic Error"));

		// Assert
		// Use the actual constant
		assertEquals(utility.CACHE_DATA, result.getDataCode());
		verify(cacheService).get(city);
	}
}