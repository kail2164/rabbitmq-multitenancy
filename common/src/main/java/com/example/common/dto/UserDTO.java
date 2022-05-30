package com.example.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO extends BaseDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5726693085950546656L;
	private String username;
	private String password;
	private String role;	
}
