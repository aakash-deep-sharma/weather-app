package com.pub.sapient.be.controller;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.pub.sapient.be.dto.WeatherForecastDto;
import com.pub.sapient.be.dto.WeatherResponseDto;
import com.pub.sapient.be.service.AsynWeatherSearvice;
import com.pub.sapient.be.service.WeatherCacheService;
import com.pub.sapient.be.service.WeatherService;
import com.pub.sapient.be.util.Utility;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private WeatherService weatherService;

	@MockitoBean
	private AsynWeatherSearvice asynWeatherSearvice;

	@MockitoBean
	private WeatherCacheService weatherCacheService;

	@MockitoBean
	private Utility utility;

	@Test
	void get3DayForecast_ShouldReturn200AndAddLinks() throws Exception {
		String city = "Ajmer";
		WeatherResponseDto mockResponse = new WeatherResponseDto(city, "LIVE_DATA", List.of());
		when(weatherService.get3DayForecast(eq(city), anyBoolean())).thenReturn(mockResponse);

		mockMvc.perform(get("/api/weather").param("city", city)).andExpect(status().isOk())
				.andExpect(jsonPath("$.city").value(city)).andExpect(jsonPath("$._links.self.href").exists());
	}

	@Test
	void get3DayForecast_ShouldAddEmergencyLink_WhenAlertExists() throws Exception {
		String city = "Ajmer";
		WeatherForecastDto forecast = new WeatherForecastDto("2026-07-02", 45.0, 30.0, List.of("HEAT_ALERT"));
		WeatherResponseDto mockResponse = new WeatherResponseDto(city, "LIVE_DATA", List.of(forecast));

		when(weatherService.get3DayForecast(eq(city), anyBoolean())).thenReturn(mockResponse);

		mockMvc.perform(get("/api/weather").param("city", city)).andExpect(status().isOk())
				.andExpect(jsonPath("$._links.emergency_offline_view").exists());
	}

	@Test
	void getCitiesForecast_ShouldReturnCollectionOfResponses() throws Exception {
		String cities = "Ajmer,Jaipur";
		WeatherResponseDto res1 = new WeatherResponseDto("Ajmer", "LIVE", List.of());
		WeatherResponseDto res2 = new WeatherResponseDto("Jaipur", "LIVE", List.of());

		when(asynWeatherSearvice.getWeatherAsync("Ajmer")).thenReturn(CompletableFuture.completedFuture(res1));
		when(asynWeatherSearvice.getWeatherAsync("Jaipur")).thenReturn(CompletableFuture.completedFuture(res2));

		mockMvc.perform(get("/api/weather/multiple-cities").param("cities", cities)).andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.weatherResponseDtoList.length()").value(2))
				.andExpect(jsonPath("$._embedded.weatherResponseDtoList[0].city").value("Ajmer"));
	}
}