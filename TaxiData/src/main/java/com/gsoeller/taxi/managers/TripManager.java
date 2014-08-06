package com.gsoeller.taxi.managers;

import java.net.UnknownHostException;
import java.util.List;

import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;

import com.google.common.collect.Lists;
import com.gsoeller.taxi.pojos.Location;
import com.gsoeller.taxi.pojos.Trip;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class TripManager {
	private JacksonDBCollection<Trip, String> collection;
	private final int MAX_DISTANCE_IN_METERS = 1000;
	
	public TripManager() throws UnknownHostException, MongoException {
		Mongo mongo = new Mongo("127.0.0.1", 27017);
		DB db = mongo.getDB("Taxi");
		collection = JacksonDBCollection.wrap(db.getCollection("trips"),
				Trip.class, String.class);
		collection.createIndex(new BasicDBObject("startLocation", "2dsphere"));
	}

	public List<Trip> getTrips(int limit) {
		DBCursor<Trip> dbCursor = collection.find().limit(limit);
		return Lists.newArrayList(dbCursor.iterator());
	}

	public Trip createTrip(Trip trip) {
		collection.insert(trip);
		return trip;
	}

	public List<Trip> getTripsWithinRadius(Location location) {
		BasicDBList locations = new BasicDBList();
		locations.put(0, location.getCoordinates().get(0));
		locations.put(1, location.getCoordinates().get(1));
		DBCursor<Trip> mydoc = collection.find(new BasicDBObject("startLocation",
				new BasicDBObject("$near", new BasicDBObject("$geometry",
						new BasicDBObject("type", "Point").append(
								"coordinates", locations)).append(
						"$maxDistance", MAX_DISTANCE_IN_METERS))));
		return Lists.newArrayList(mydoc.iterator());
	}
	
}
