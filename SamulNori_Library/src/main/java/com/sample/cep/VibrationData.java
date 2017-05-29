package com.sample.cep;

public class VibrationData extends SensorData {
	public VibrationData(long value) {
		setVibration(value);
		setWriteValue(1<<2);
	}
	
	public double getVibration() {
		return getLongValue();
	}
	public void setVibration(long value) {
		setLongValue(value);
	}
	public long getVibrationLimit()
	{
		return getLongLimit();
	}
	public void setVibrationLimit(long value)
	{
		setLongLimit(value);
	}
}