package com.pub.sapient.be.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherPayload {

	private List<ForecastData> list;

	public List<ForecastData> getList() {
		return list;
	}

	public void setList(List<ForecastData> list) {
		this.list = list;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ForecastData {
		@JsonProperty("dt_txt")
		private String dtTxt;
		private MainData main;
		private List<WeatherCondition> weather;
		private WindData wind;

		public String getDtTxt() {
			return dtTxt;
		}

		public void setDtTxt(String dtTxt) {
			this.dtTxt = dtTxt;
		}

		public MainData getMain() {
			return main;
		}

		public void setMain(MainData main) {
			this.main = main;
		}

		public List<WeatherCondition> getWeather() {
			return weather;
		}

		public void setWeather(List<WeatherCondition> weather) {
			this.weather = weather;
		}

		public WindData getWind() {
			return wind;
		}

		public void setWind(WindData wind) {
			this.wind = wind;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class MainData {
		@JsonProperty("temp_max")
		private double tempMax;
		@JsonProperty("temp_min")
		private double tempMin;

		public double getTempMax() {
			return tempMax;
		}

		public void setTempMax(double tempMax) {
			this.tempMax = tempMax;
		}

		public double getMinTemp() {
			return tempMin;
		}

		public void setTempMin(double tempMin) {
			this.tempMin = tempMin;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class WeatherCondition {
		private String main;
		private String description;

		public String getMain() {
			return main;
		}

		public void setMain(String main) {
			this.main = main;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class WindData {
		private double speed;

		public double getSpeed() {
			return speed;
		}

		public void setSpeed(double speed) {
			this.speed = speed;
		}
	}
}
