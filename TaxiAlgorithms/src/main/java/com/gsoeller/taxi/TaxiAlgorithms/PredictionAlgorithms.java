package com.gsoeller.taxi.TaxiAlgorithms;

import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.gsoeller.taxi.managers.TripManager;
import com.gsoeller.taxi.pojos.Location;
import com.gsoeller.taxi.pojos.Trip;

public class PredictionAlgorithms {
	
	private TripManager tripManager;
	private static final double EPS = .0001;
	private static final int MIN_POINTS = 10;
	private final DBSCANClusterer<DoublePoint> dbscan = new DBSCANClusterer<DoublePoint>(EPS, MIN_POINTS); 
	
	@Inject
	public PredictionAlgorithms(TripManager tripManager) {
		this.tripManager = tripManager;
	}
	
	public Location predictLocation(Location start) {
		// get all the trips that started close to the given location
		// create a graph to cluster trips together
		// find the best cluster of trips
		// pick a location in the best cluster as a center point 
		List<Cluster<DoublePoint>> clusters = getClusters(start);
		int maxSize = 0;
		Optional<Cluster<DoublePoint>> bestCluster = Optional.absent();
		for(Cluster<DoublePoint> cluster: clusters) {
			int nextClusterSize = cluster.getPoints().size();
			if (nextClusterSize > maxSize) {
				maxSize = nextClusterSize;
				bestCluster = Optional.of(cluster);
			}
		}
		return null;
	}
	
	private List<Cluster<DoublePoint>> getClusters(Location start) {
		Collection<Trip> locations = tripManager.getTripsWithinRadius(start);
		if (locations.size() > 0) {
			return dbscan.cluster(getPoints(locations));
		}
		throw new RuntimeException("No trips");
	}
	
	private Collection<DoublePoint> getPoints(Collection<Trip> trips) {
		List<DoublePoint> points = Lists.newArrayList();
		for(Trip trip: trips) {
			double[] coord = {trip.getEndLocation().getCoordinates().get(0), trip.getEndLocation().getCoordinates().get(1)};
			points.add(new DoublePoint(coord));
		}
		return points;
	}
}
