package com.gsoeller.taxi.TaxiAlgorithms;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class PredictionModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PredictionAlgorithms.class).in(Singleton.class);
	}

}
