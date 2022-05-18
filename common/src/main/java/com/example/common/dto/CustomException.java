package com.example.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CustomException extends Exception {
	/**
	* 
	*/
	private static final long serialVersionUID = -2115930794746723071L;
	private APIStatus status;

	public CustomException(String message) {
		super(message);
		this.status = APIStatus.BAD_REQUEST;
	}

	public CustomException(APIStatus status, String message) {
		super(message);
		this.status = status;
	}
}
