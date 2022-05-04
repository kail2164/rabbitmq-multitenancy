package com.example.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends Exception {
	/**
	* 
	*/
	private static final long serialVersionUID = -2115930794746723071L;
	private APIStatus status;

	public CustomException() {
	}

	public CustomException(String message) {
		super(message);
		this.status = APIStatus.BAD_REQUEST;
	}

	public CustomException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomException(Throwable cause) {
		super(cause);
	}

	public CustomException(APIStatus status) {
		this.status = status;
	}

	public CustomException(APIStatus status, String message) {
		super(message);
		this.status = status;
	}

	public APIStatus getStatus() {
		return status;
	}
}
