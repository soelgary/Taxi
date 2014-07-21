package com.gsoeller.taxi.pojos;

import java.util.List;

import net.vz.mongodb.jackson.ObjectId;

import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

public class Trip {
	private DateTime startTime;
	private DateTime endTime;
	private List<Double> startLocation;
	private List<Double> endLocation;
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

	public List<Double> getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(List<Double> startLocation) {
		this.startLocation = startLocation;
	}

	public List<Double> getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(List<Double> endLocation) {
		this.endLocation = endLocation;
	}

}
