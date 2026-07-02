package com.pub.sapient.be.service;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.pub.sapient.be.model.OpenWeatherPayload;

@Service
public class WeatherCacheService {
	private final CacheManager cacheManager;

	public WeatherCacheService(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void put(String city, OpenWeatherPayload data) {
		Cache cache = cacheManager.getCache("weatherCache");
		if (cache != null) {
			cache.put(city.toLowerCase(), data);
		}
	}

	public OpenWeatherPayload get(String city) {
		Cache cache = cacheManager.getCache("weatherCache");
		return cache != null ? cache.get(city.toLowerCase(), OpenWeatherPayload.class) : null;
	}
}
