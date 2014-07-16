package com.gsoeller.taxi.TaxiData;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.gsoeller.taxi.managers.TripManager;


public class DataModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(TripManager.class).in(Singleton.class);
	}

}
