package com.gsoeller.taxi.TaxiAlgorithms;

import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.inject.Inject;
import com.gsoeller.taxi.managers.TripManager;
import com.gsoeller.taxi.pojos.Location;
import com.gsoeller.taxi.pojos.Trip;

public class PredictionAlgorithms {
	
	private TripManager tripManager;
	private static final double EPS = .0001;
	private static final int MIN_POINTS = 10;
	private final Logger LOG = LoggerFactory.getLogger(PredictionAlgorithms.class);

	private final int R = 6371;
	private final DBSCANClusterer<DoublePoint> dbscan = new DBSCANClusterer<DoublePoint>(EPS, MIN_POINTS);
	private final Location PENN_STATION_LOCATION = new Location(Lists.newArrayList(-73.991142, 40.750538));
	private final Location AIRPORT_LOCATION = new Location(Lists.newArrayList(-73.861183, 40.767696));
	
	private final List<Location> BLACKLIST_LOCATIONS = Lists.newArrayList();

	
	@Inject
	public PredictionAlgorithms(TripManager tripManager) {
		this.tripManager = tripManager;
	}
	
	@Timed
	public Optional<Location> predictLocation(Location start) {
		// get all the trips that started close to the given location
		// create a graph to cluster trips together
		// find the best cluster of trips
		// pick a location in the best cluster as a center point 
		
		
		List<Cluster<DoublePoint>> clusters = getClusters(start);
		

		Ordering<Cluster<DoublePoint>> ordering = new Ordering<Cluster<DoublePoint>> () {

			@Override
			public int compare(Cluster<DoublePoint> cluster1,
					Cluster<DoublePoint> cluster2) {
				return cluster2.getPoints().size() - cluster1.getPoints().size();
			}
			
		};
		
		Iterable<Cluster<DoublePoint>> orderedClusters = ordering.sortedCopy(clusters);
		
		for (Cluster<DoublePoint> cluster: orderedClusters) {
			Optional<Location> bestLocation = getBestLocation(cluster, start);
			if (bestLocation.isPresent()) {
				return bestLocation;
			}
		}
		return Optional.absent();
		
	}
	
	private Optional<Location> getBestLocation(Cluster<DoublePoint> cluster, Location start) {
		
		Optional<Location> centroid = computeCentroid(cluster);
		if (!centroid.isPresent()) {
			return Optional.absent();
		}
		Optional<Location> bestPoint = Optional.absent();
		double smallestDistance = 0;
		for(int i = 0; i < cluster.getPoints().size(); i++) {
			Location point = new Location();
			point.setCoordinates(Lists.newArrayList(cluster.getPoints().get(0).getPoint()[0], cluster.getPoints().get(0).getPoint()[1]));
			double nextDistance = getDistance(centroid.get(), point);
			double distanceFromStart = getDistance(point, start);
			if ((!bestPoint.isPresent() || (bestPoint.isPresent() && nextDistance < smallestDistance)) &&
					distanceFromStart > .75) {
				boolean isInBlacklistedLocation = false;
				for (Location blacklisted: BLACKLIST_LOCATIONS) {
					if (getDistance(point, blacklisted) < 10) {
						isInBlacklistedLocation = true;
					}
				}
				if (!isInBlacklistedLocation) {
					bestPoint = Optional.of(point);
					smallestDistance = nextDistance;
				}
			}
		}
		return bestPoint;
	}
	
	private double getDistance(Location centroid, Location point) {
		double centroidLat = centroid.getCoordinates().get(0);
		double pointLat = point.getCoordinates().get(0);
		double latDistance = toRadians(centroidLat - pointLat);
        double lonDistance = toRadians(centroid.getCoordinates().get(1) - point.getCoordinates().get(1));
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRadians(centroidLat)) * Math.cos(toRadians(pointLat)) *
                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
	}
	
	private Optional<Location> computeCentroid(Cluster<DoublePoint> cluster) {
		double x = 0;
		double y = 0;
		for (int i=0; i < cluster.getPoints().size(); i++) {
			x += cluster.getPoints().get(i).getPoint()[0];
			y += cluster.getPoints().get(i).getPoint()[1];
		}
		x = x/cluster.getPoints().size();
		y = y/cluster.getPoints().size();
		Location loc = new Location();
		
		loc.setCoordinates(Lists.newArrayList(x, y));
		if (cluster.getPoints().size() > 0) {
			return Optional.of(loc);
		}
		return Optional.absent();
	}
	
	private List<Cluster<DoublePoint>> getClusters(Location start) {
		Collection<Trip> locations = tripManager.getTripsWithinRadius(start);
		if (locations.size() > 0) {
			Stopwatch timer = Stopwatch.createStarted();
			List<Cluster<DoublePoint>> clusters = dbscan.cluster(getPoints(locations));
			LOG.info("Method took: " + timer.stop());
			return clusters;
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
	
	private double toRadians(double value) {
		return value * Math.PI / 180;
	}
}
