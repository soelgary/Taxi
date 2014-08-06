package com.gsoeller.taxi.pojos;

import java.util.List;
import java.util.Objects;

public class Location {

	private String type;
	private List<Double> coordinates;
	
	public Location() {}
	
	public Location(List<Double> coordinates) {
		this.coordinates = coordinates;
		this.type = "Point";
	}

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
	
	
	public boolean equals(Object that) {
		if (that instanceof Location) {
			Location thatLocation = (Location)that;
			return thatLocation.getType().equals(this.getType()) &&
					thatLocation.getCoordinates().equals(this.getCoordinates());
		}
		return false;
	}
	
	public int hashCode() {
		return Objects.hashCode(getType()) + Objects.hashCode(getCoordinates());
	}
}
