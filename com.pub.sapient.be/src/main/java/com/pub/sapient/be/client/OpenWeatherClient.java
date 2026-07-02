package com.pub.sapient.be.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pub.sapient.be.model.OpenWeatherPayload;

import io.github.resilience4j.retry.annotation.Retry;

@FeignClient(name = "openWeatherClient", url = "${weather.api.base-url}")
public interface OpenWeatherClient {

	@GetMapping("${weather.api.endpoints.forecast}")
	@Retry(name = "openWeatherRetry")
	OpenWeatherPayload fetch3DayForecast(@RequestParam("q") String city, @RequestParam("appid") String apiKey,
			@RequestParam("cnt") int count);
}
