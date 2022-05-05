package com.example.common.dto.response;

import com.example.common.dto.BaseAccount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponse extends BaseAccount {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7999310067588462561L;
	private String token;
	private String role;
}
