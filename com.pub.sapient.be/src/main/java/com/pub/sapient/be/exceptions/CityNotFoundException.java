package com.pub.sapient.be.exceptions;

public class CityNotFoundException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 5869600911675963762L;
	private final String city;

	public CityNotFoundException(String city, String message) {
		super(message);
		this.city = city;
	}

	public String getCity() {
		return city;
	}
}
