package com.sample.cep;

public class ThermoSensorData extends SensorData {
	public ThermoSensorData(double value) {
		setTemperature(value);
	}
	
	public double getTemperature() {
		return getDoubleValue();
	}
	
	public void setTemperature(double value) {
		setDoubleValue(value);
	}
}