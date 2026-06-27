package com.pub.sapient.be.dto;

import java.util.List;

public class WeatherResponseDto {

	private String city;
	private String notes;
	private List<WeatherForecastDto> forecasts;
	private String dataCode;

	public String getDataCode() {
		return dataCode;
	}

	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}

	public WeatherResponseDto() {
	}

	public WeatherResponseDto(String city, String notes, List<WeatherForecastDto> forecasts, String dataCode) {
		this.city = city;
		this.notes = notes;
		this.forecasts = forecasts;
		this.dataCode = dataCode;
	}

	// Getters and Setters
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<WeatherForecastDto> getForecasts() {
		return forecasts;
	}

	public void setForecasts(List<WeatherForecastDto> forecasts) {
		this.forecasts = forecasts;
	}
}
