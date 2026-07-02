package com.pub.sapient.be.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.pub.sapient.be.model.OpenWeatherPayload;

@ExtendWith(MockitoExtension.class)
class WeatherCacheServiceTest {

	@Mock
	private CacheManager cacheManager;
	@Mock
	private Cache cache;

	@InjectMocks
	private WeatherCacheService weatherCacheService;

	@Test
	void put_ShouldPutDataInCacheWithLowercaseKey() {
		// Arrange
		String city = "London";
		OpenWeatherPayload payload = new OpenWeatherPayload();
		when(cacheManager.getCache("weatherCache")).thenReturn(cache);

		// Act
		weatherCacheService.put(city, payload);

		// Assert
		verify(cache).put("london", payload);
	}

	@Test
	void get_ShouldReturnDataFromCache() {
		// Arrange
		String city = "London";
		OpenWeatherPayload expectedPayload = new OpenWeatherPayload();
		when(cacheManager.getCache("weatherCache")).thenReturn(cache);
		when(cache.get("london", OpenWeatherPayload.class)).thenReturn(expectedPayload);

		// Act
		OpenWeatherPayload actualPayload = weatherCacheService.get(city);

		// Assert
		assertEquals(expectedPayload, actualPayload);
		verify(cache).get("london", OpenWeatherPayload.class);
	}

	@Test
	void get_ShouldReturnNullWhenCacheIsNull() {
		// Arrange
		when(cacheManager.getCache("weatherCache")).thenReturn(null);

		// Act
		OpenWeatherPayload result = weatherCacheService.get("AnyCity");

		// Assert
		assertNull(result);
	}
}