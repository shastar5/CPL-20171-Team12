package com.sample.cep;

public class writeValue {
	private int WriteValue = 0;
	
	public writeValue() {
		this.WriteValue = 0;
	}
	public void addWriteValue(int value)
	{
		if((~WriteValue & value) == value)
			this.WriteValue += value;		
	}
	public void subWriteValue(int value)
	{
		if((WriteValue & value) == value)
			this.WriteValue -= value;
	}
	public int getWriteValue()
	{
		return WriteValue;
	}
	public void setWriteValue(int value)
	{
		this.WriteValue = value;
	}
	/*
	 * 1 : DUST
	 * 1 << 1 : SMOKE
	 * 1 << 2 : Vibration
	 * 1 << 3 : fingerPrint
	 */
}
