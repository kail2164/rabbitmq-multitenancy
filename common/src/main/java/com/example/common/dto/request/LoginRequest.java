package com.example.common.dto.request;

import com.example.common.dto.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest extends BaseDTO {

	private static final long serialVersionUID = 7080743203030168544L;
	
	private String username;
	private String password;	
}
