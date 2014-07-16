package com.gsoeller.taxi.pojos;

import net.vz.mongodb.jackson.ObjectId;

import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

public class Trip {
	private DateTime startTime;
	private DateTime endTime;
	private Location startLocation;
	private Location endLocation;
	private String id;
	
	@ObjectId
	@JsonProperty("_id")
	public String getId() {
		return id;
	}
	
	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

	public Location getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(Location startLocation) {
		this.startLocation = startLocation;
	}

	public Location getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(Location endLocation) {
		this.endLocation = endLocation;
	}

}
