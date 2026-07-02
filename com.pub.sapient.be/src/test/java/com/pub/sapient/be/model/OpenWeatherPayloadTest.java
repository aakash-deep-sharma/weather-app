package com.pub.sapient.be.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class OpenWeatherPayloadTest {

	@Test
	void testJsonDeserialization() throws Exception {
		String json = """
				{
				  "list": [
				    {
				      "dt_txt": "2026-07-02 12:00:00",
				      "main": {
				        "temp_max": 305.0,
				        "temp_min": 295.0
				      },
				      "weather": [{"main": "Rain", "description": "heavy rain"}],
				      "wind": {"speed": 12.5}
				    }
				  ]
				}
				""";

		ObjectMapper mapper = new ObjectMapper();
		OpenWeatherPayload payload = mapper.readValue(json, OpenWeatherPayload.class);

		// Verify mapping
		assertNotNull(payload.getList());
		assertEquals(1, payload.getList().size());

		var firstForecast = payload.getList().get(0);
		assertEquals("2026-07-02 12:00:00", firstForecast.getDtTxt());
		assertEquals(305.0, firstForecast.getMain().getTempMax());
		assertEquals("Rain", firstForecast.getWeather().get(0).getMain());
		assertEquals(12.5, firstForecast.getWind().getSpeed());
	}
}