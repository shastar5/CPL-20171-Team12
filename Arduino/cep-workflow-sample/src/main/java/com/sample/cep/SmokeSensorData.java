package com.sample.cep;

public class SmokeSensorData extends SensorData {
	public SmokeSensorData(double value) {
		setSmokeLevel(value);
	}
	
	public double getSmokeLevel() {
		return getDoubleValue();
	}
	
	public void setSmokeLevel(double value) {
		setDoubleValue(value);
	}
}