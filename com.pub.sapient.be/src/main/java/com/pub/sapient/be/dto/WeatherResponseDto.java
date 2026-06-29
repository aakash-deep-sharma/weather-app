package com.pub.sapient.be.dto;

import java.util.List;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

public class WeatherResponseDto extends RepresentationModel<WeatherResponseDto> {
	private String city;
	private String notes;
	private List<WeatherForecastDto> forecasts;
	private String dataCode;

	public WeatherResponseDto() {
	}

	public WeatherResponseDto(String city, String notes, List<WeatherForecastDto> forecasts, String dataCode) {
		this.city = city;
		this.notes = notes;
		this.forecasts = forecasts;
		this.dataCode = dataCode;
	}

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

	public String getDataCode() {
		return dataCode;
	}

	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false; // Important: includes Spring HATEOAS links comparison
		}
		WeatherResponseDto that = (WeatherResponseDto) o;
		return Objects.equals(city, that.city) && Objects.equals(notes, that.notes)
				&& Objects.equals(forecasts, that.forecasts) && Objects.equals(dataCode, that.dataCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), city, notes, forecasts, dataCode);
	}
}