package com.niqactivate.exception;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

	private final int status;
	private final String error;

	public ErrorResponse(HttpStatus status, String error) {
		this.status = status.value();
		this.error = error;
	}

	public int getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}
}
