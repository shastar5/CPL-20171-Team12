package com.sample.cep;

public class FingerPrintData extends SensorData{
	public FingerPrintData(long value) {
		setFingerPrint(value);
	}
	
	public double getFingerPrint() {
		return getLongValue();
	}
	
	public void setFingerPrint(long value) {
		setLongValue(value);
	}
}
