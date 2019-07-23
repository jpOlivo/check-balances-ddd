package com.nu.exception;

@SuppressWarnings("serial")
public class OperationNotCompletedException extends RuntimeException {

	public OperationNotCompletedException(String message) {
		super(message);
	}

}
