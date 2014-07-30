package com.gsoeller.taxi.TaxiService;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.gsoeller.taxi.TaxiAlgorithms.PredictionAlgorithms;
import com.gsoeller.taxi.TaxiAlgorithms.PredictionModule;
import com.gsoeller.taxi.TaxiData.DataModule;
import com.gsoeller.taxi.managers.TripManager;
import com.gsoeller.taxi.resources.TripResource;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TaxiServiceApplication extends Application<TaxiServiceConfiguration> {

	public static void main(String[] args) throws Exception {
		new TaxiServiceApplication().run(args);
	}
	
	@Override
	public void initialize(Bootstrap<TaxiServiceConfiguration> bootstrap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run(TaxiServiceConfiguration conf, Environment env)
			throws Exception {
		Injector injector = Guice.createInjector(new DataModule(), new PredictionModule());
		TripManager tripManager = injector.getInstance(TripManager.class);
		env.jersey().register(new TripResource(tripManager, new PredictionAlgorithms(tripManager)));
	}

}
