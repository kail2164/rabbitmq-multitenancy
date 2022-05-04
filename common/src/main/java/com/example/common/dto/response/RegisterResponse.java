package com.example.common.dto.response;

import com.example.common.dto.BaseAccount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponse extends BaseAccount {
	private String token;
	private String role;
}
