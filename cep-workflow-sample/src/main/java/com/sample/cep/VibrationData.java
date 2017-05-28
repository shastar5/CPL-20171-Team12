package com.sample.cep;

public class VibrationData extends SensorData {
	public VibrationData(long value) {
		setVibration(value);
	}
	
	public double getVibration() {
		return getLongValue();
	}
	
	public void setVibration(long value) {
		setLongValue(value);
	}
}