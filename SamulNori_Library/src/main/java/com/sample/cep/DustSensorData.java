package com.sample.cep;

public class DustSensorData extends SensorData {
	public DustSensorData(double value) {
		setDust(value);
	}
	
	public double getDust() {
		return getDoubleValue();
	}
	
	public void setDust(double value) {
		setDoubleValue(value);
	}
}