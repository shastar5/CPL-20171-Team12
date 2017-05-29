package com.sample.cep;

public class DustSensorData extends SensorData {
	public DustSensorData(double value) {
		setDust(value);
		setWriteValue(1);
	}
	public double getDust() {
		return getDoubleValue();
	}
	public void setDust(double value) {
		setDoubleValue(value);
	}
	public double getDustLimit()
	{
		return getDoubleLimit();
	}
	public void setDustLimit(double value)
	{
		setDoubleLimit(value);
	}
}