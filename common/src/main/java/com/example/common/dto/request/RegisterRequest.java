package com.example.common.dto.request;

import com.example.common.dto.BaseAccount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest extends BaseAccount{
	/**
	 * 
	 */
	private static final long serialVersionUID = 90632454874452524L;	
	private String password;
}
