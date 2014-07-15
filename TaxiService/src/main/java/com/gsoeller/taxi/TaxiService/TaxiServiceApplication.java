package com.gsoeller.taxi.TaxiService;

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
		env.jersey().register(new TripResource());
	}

}
