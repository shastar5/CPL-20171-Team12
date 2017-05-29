package com.sample.cep;

public class SensorData {
	private String type;
	private String id;
	private double doubleValue;
	private long longValue;
	
	private long longLimit = -1;
	private double doubleLimit = -1;
	
	/*
	 *
	 * 
	 * 
	*/
	
	
	private long eventValue = -1;
	private long eventCancelValue = -1;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getDoubleValue() {
		return doubleValue;
	}
	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}
	public long getLongValue() {
		return longValue;
	}
	public void setLongValue(long longValue) {
		this.longValue = longValue;
	}
	public void setLongLimit(long longValue) {
		this.longLimit = longValue;
	}
	public long getLongLimit() {
		return longLimit;
	}
	public void setDoubleLimit(double doubleValue) {
		this.doubleLimit = doubleValue;
	}
	public double getDoubleLimit() {
		return doubleLimit;
	}
	public void setEventValue(long longValue) {
		this.eventValue = longValue;
	}
	public long getEventValue() {
		return eventValue;
	}
	public void setEventCancelValue(long longValue) {
		this.eventCancelValue = longValue;
	}
	public long getEventCancelValue() {
		return this.eventCancelValue;
	}
}

