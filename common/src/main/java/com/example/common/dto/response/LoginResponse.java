package com.example.common.dto.response;

import com.example.common.dto.BaseAccount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends BaseAccount{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6048972744293737996L;
	private String token;
	private String role;
}
