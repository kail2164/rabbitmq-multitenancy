package com.example.common.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7641338099027110202L;
	private String username;
	private String password;
}
