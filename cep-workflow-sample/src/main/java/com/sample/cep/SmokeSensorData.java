package com.sample.cep;

public class SmokeSensorData extends SensorData {
	private int as;
	public SmokeSensorData(double value) {
		setSmokeLevel(value);
	}
	
	public double getSmokeLevel() {
		return getDoubleValue();
	}
	
	public void setSmokeLevel(double value) {
		setDoubleValue(value);
	}
	
	public void seta(int a)
	{
		as = a;
	}
	public int geta()
	{
		return as;
	}
}