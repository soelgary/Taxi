package com.gsoeller.taxi.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.gsoeller.taxi.managers.TripManager;
import com.gsoeller.taxi.pojos.Trip;

@Path("/trip")
@Produces(MediaType.APPLICATION_JSON)
public class TripResource {

	private TripManager manager;
	private final int DEFAULT_LIMIT = 100;
	
	@Inject
	public TripResource(TripManager manager) {
		this.manager = manager;
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
		return manager.createTrip(trip);
	}
}