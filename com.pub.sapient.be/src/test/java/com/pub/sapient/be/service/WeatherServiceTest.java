package com.pub.sapient.be.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pub.sapient.be.dto.WeatherResponseDto;
import com.pub.sapient.be.model.OpenWeatherPayload;
import com.pub.sapient.be.util.Utility;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

	@Mock
	private WeatherClientService clientService;
	@Mock
	private WeatherCacheService cacheService;
	@Mock
	private Utility utility;

	@InjectMocks
	private WeatherService weatherService;

	@Test
	void get3DayForecast_OfflineMode_ReturnsCachedData() {
		// Arrange
		String city = "Ajmer";
		OpenWeatherPayload mockPayload = new OpenWeatherPayload();
		when(cacheService.get(city)).thenReturn(mockPayload);
		when(utility.processPayload(eq(city), any(), any())).thenReturn(new WeatherResponseDto());

		// Act
		WeatherResponseDto result = weatherService.get3DayForecast(city, true);

		// Assert
		assertNotNull(result);
		verify(clientService, never()).fetchForecast(anyString());
		verify(cacheService).get(city);
	}

	@Test
	void get3DayForecast_OnlineMode_CallsClientAndCaches() {
		// Arrange
		String city = "Ajmer";
		OpenWeatherPayload mockPayload = new OpenWeatherPayload();
		when(clientService.fetchForecast(city)).thenReturn(mockPayload);
		when(utility.processPayload(eq(city), any(), any())).thenReturn(new WeatherResponseDto());

		// Act
		weatherService.get3DayForecast(city, false);

		// Assert
		verify(clientService).fetchForecast(city);
		verify(cacheService).put(city, mockPayload);
	}
}
