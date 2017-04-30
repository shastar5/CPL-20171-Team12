package com.sample.cep;

public class FanActuatorData extends SensorData {
	private boolean on;
	
	public FanActuatorData(boolean value) {
		setOn(value);
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}
	
	
}
