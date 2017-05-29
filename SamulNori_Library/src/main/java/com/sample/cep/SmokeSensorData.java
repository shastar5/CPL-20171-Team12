package com.sample.cep;

public class SmokeSensorData extends SensorData {
	public SmokeSensorData(double value) {
		setSmokeLevel(value);
		setWriteValue(1<<1);
	}
	public double getSmokeLevel() {
		return getDoubleValue();
	}
	public void setSmokeLevel(double value) {
		setDoubleValue(value);
	}
	public long getSmokeLimit()
	{
		return getLongLimit();
	}
	public void setSmokeLimit(long value)
	{
		setLongLimit(value);
	}
}