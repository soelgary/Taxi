package com.gsoeller.taxi.pojos;

import java.util.List;

import com.google.common.collect.Lists;

public class Location {
	private List<Double> point = Lists.newArrayList(0.0, 0.0); 

	public double getLongitude() {
		return point.get(0);
	}

	public void setLongitude(double longitude) {
		point.set(0, longitude);
	}

	public double getLatitude() {
		return point.get(1);
	}

	public void setLatitude(double latitude) {
		point.set(0, latitude);
	}

}
