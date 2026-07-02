package com.pub.sapient.be.dto;

import java.util.List;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

public class WeatherResponseDto extends RepresentationModel<WeatherResponseDto> {
	private String city;
	private String notes;
	private List<WeatherForecastDto> forecasts;

	public WeatherResponseDto() {
	}

	public WeatherResponseDto(String city, String notes, List<WeatherForecastDto> forecasts) {
		this.city = city;
		this.notes = notes;
		this.forecasts = forecasts;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(city, forecasts, notes);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		WeatherResponseDto other = (WeatherResponseDto) obj;
		return Objects.equals(city, other.city) && Objects.equals(forecasts, other.forecasts)
				&& Objects.equals(notes, other.notes);
	}

}