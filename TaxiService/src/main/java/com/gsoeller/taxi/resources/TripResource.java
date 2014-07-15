package com.gsoeller.taxi.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/trip")
@Produces(MediaType.APPLICATION_JSON)
public class TripResource {

	@GET
	public boolean test() {
		return true;
	}
}
