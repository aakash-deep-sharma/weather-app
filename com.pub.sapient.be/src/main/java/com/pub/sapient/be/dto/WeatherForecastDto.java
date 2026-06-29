package com.pub.sapient.be.dto;

import java.util.List;

public class WeatherForecastDto {
	private String date;
	private double maxTemp;
	private double minTemp;
	private List<String> alerts;

	public WeatherForecastDto(String date, double maxTemp, double minTemp, List<String> alerts) {
		this.date = date;
		this.maxTemp = maxTemp;
		this.minTemp = minTemp;
		this.alerts = alerts;
	}

	// Getters and Setters
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(double maxTemp) {
		this.maxTemp = maxTemp;
	}

	public double getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(double minTemp) {
		this.minTemp = minTemp;
	}

	public List<String> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<String> alerts) {
		this.alerts = alerts;
	}
}
