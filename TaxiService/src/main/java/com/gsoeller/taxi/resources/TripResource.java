package com.gsoeller.taxi.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.gsoeller.taxi.TaxiAlgorithms.PredictionAlgorithms;
import com.gsoeller.taxi.managers.TripManager;
import com.gsoeller.taxi.pojos.Location;
import com.gsoeller.taxi.pojos.Trip;

@Path("/trip")
@Produces(MediaType.APPLICATION_JSON)
public class TripResource {

	private TripManager manager;
	private final int DEFAULT_LIMIT = 100;
	private PredictionAlgorithms algorithms;
	
	@Inject
	public TripResource(TripManager manager, PredictionAlgorithms algorithms) {
		this.manager = manager;
		this.algorithms = algorithms;
	}
	
	@GET
	public List<Trip> getTrips(@QueryParam("limit") Optional<Integer> limit) {
		int actualLimit = DEFAULT_LIMIT;
		if (limit.isPresent()) {
			actualLimit = limit.get();
		}
		return manager.getTrips(actualLimit);
	}
	
	@POST
	public Trip addTrip(Trip trip) {
		Preconditions.checkArgument(trip.getStartLocation().getCoordinates().get(0) > 33 &&
				trip.getStartLocation().getCoordinates().get(0) < 47, "Start not within latitude range", trip.getStartLocation().getCoordinates().get(0));
		Preconditions.checkArgument(trip.getStartLocation().getCoordinates().get(1) < -67 && 
				trip.getStartLocation().getCoordinates().get(1) > -80, "Start not within longitude range", trip.getStartLocation().getCoordinates().get(1));
		
		Preconditions.checkArgument(trip.getEndLocation().getCoordinates().get(0) > 33 &&
				trip.getEndLocation().getCoordinates().get(0) < 47, "End not within latitude range", trip.getEndLocation().getCoordinates().get(0));
		Preconditions.checkArgument(trip.getEndLocation().getCoordinates().get(1) < -67 && 
				trip.getEndLocation().getCoordinates().get(1) > -80, "End not within longitude range", trip.getEndLocation().getCoordinates().get(1));
		return manager.createTrip(trip);
	}
	
	@GET
	@Path("/filter")
	public List<Trip> filterTrips(@QueryParam("latitude") double lat, @QueryParam("longitude") double lon){
		Location loc = new Location();
		loc.setCoordinates(Lists.newArrayList(lat, lon));
		loc.setType("Point");
		return manager.getTripsWithinRadius(loc);
	}
	
	@GET
	@Path("/predict")
	public Location predictEndLocation(@QueryParam("latitude") double lat, @QueryParam("longitude") double lon){
		Location loc = new Location();
		loc.setCoordinates(Lists.newArrayList(lat, lon));
		loc.setType("Point");
		return algorithms.predictLocation(loc);
	}
}
