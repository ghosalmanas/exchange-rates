package com.wholesale.exception;

public class NoCurrencyAvailableException extends Exception{

	private static final long serialVersionUID = 1L;

	public NoCurrencyAvailableException (String message)
	{
		super(message);
		
	}
}
