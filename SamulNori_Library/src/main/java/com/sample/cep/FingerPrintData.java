package com.sample.cep;

public class FingerPrintData extends SensorData{
	public FingerPrintData(long value) {
		setFingerPrint(value);
		setWriteValue(1<<3);
	}
	
	public double getFingerPrint() {
		return getLongValue();
	}
	
	public void setFingerPrint(long value) {
		setLongValue(value);
	}
}
