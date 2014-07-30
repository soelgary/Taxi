package com.gsoeller.taxi.pojos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Location {

	private String type;
	private List<Double> coordinates;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public List<Double> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Double> coordinates) {
		this.coordinates = coordinates;
	}
	
	@JsonIgnore
	public double getLatitude() {
		return coordinates.get(0);
	}
	
	@JsonIgnore
	public double getLongitude() {
		return coordinates.get(1);
	}
}
