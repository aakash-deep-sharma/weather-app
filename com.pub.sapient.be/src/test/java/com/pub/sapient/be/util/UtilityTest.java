package com.pub.sapient.be.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pub.sapient.be.dto.WeatherAlertDto;
import com.pub.sapient.be.dto.WeatherResponseDto;
import com.pub.sapient.be.model.OpenWeatherPayload;
import com.pub.sapient.be.model.OpenWeatherPayload.ForecastData;
import com.pub.sapient.be.model.OpenWeatherPayload.MainData;
import com.pub.sapient.be.rule.WeatherRule;

@ExtendWith(MockitoExtension.class)
class UtilityTest {

	@Mock
	private WeatherRule heatRule;
	@Mock
	private WeatherRule windRule;

	@Test
	void processPayload_AppliesAllRulesAndCollectsAlerts() {
		// Arrange
		Utility utility = new Utility(List.of(heatRule, windRule));

		OpenWeatherPayload payload = new OpenWeatherPayload();
		ForecastData fData = new ForecastData();
		fData.setDtTxt("2026-07-02 12:00:00");
		fData.setMain(new MainData());
		payload.setList(List.of(fData));

		// Mock rules returning different alert scenarios
		when(heatRule.evaluate(fData)).thenReturn(Optional.of(new WeatherAlertDto("Heat Alert")));
		when(windRule.evaluate(fData)).thenReturn(Optional.empty());

		// Act
		WeatherResponseDto result = utility.processPayload("Ajmer", payload, utility.LIVE_DATA);

		// Assert
		List<String> alerts = result.getForecasts().get(0).getAlerts();
		assertEquals(1, alerts.size());
		assertEquals("Heat Alert", alerts.get(0));

		verify(heatRule).evaluate(fData);
		verify(windRule).evaluate(fData);
	}
}