package com.niqactivate.exception;

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String message, String string, Long id) {
		super(message);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
