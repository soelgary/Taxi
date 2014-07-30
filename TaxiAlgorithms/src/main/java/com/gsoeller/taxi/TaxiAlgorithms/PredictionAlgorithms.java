package com.gsoeller.taxi.TaxiAlgorithms;

import java.util.List;

import com.google.inject.Inject;
import com.gsoeller.taxi.managers.TripManager;
import com.gsoeller.taxi.pojos.Location;
import com.gsoeller.taxi.pojos.Trip;

public class PredictionAlgorithms {
	
	private TripManager tripManager;
	
	@Inject
	public PredictionAlgorithms(TripManager tripManager) {
		this.tripManager = tripManager;
	}
	
	public Location predictLocation(Location start) {
		// get all the trips that started close to the given location
		// create a graph to cluster trips together
		// find the best cluster of trips
		// pick a location in the best cluster as a center point 
		
		List<Trip> locations = tripManager.getTripsWithinRadius(start);
		if (locations.size() > 0) {
			return locations.get(0).getEndLocation();
		}
		throw new RuntimeException("No trips");
	}
}
