package com.sample.cep;

public class DustData extends SensorData {
	public DustData(double value) {
		setDust(value);
	}
	
	public double getDust() {
		return getDoubleValue();
	}
	
	public void setDust(double value) {
		setDoubleValue(value);
	}
}