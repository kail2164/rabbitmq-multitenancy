package com.example.common.dto.response;

import com.example.common.dto.BaseAccount;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse extends BaseAccount{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6048972744293737996L;
	private String token;
	private String role;
	
	@Override
	public String toString() {	
		StringBuilder builder = new StringBuilder();
		builder.append("Token: " + token);
		builder.append(" Username: " + getUsername());
		builder.append(" Role: " + role);
		builder.append(" First Name: " + getFirstName());
		builder.append(" Last Name: " + getLastName());
		builder.append(" ID: " + getId());
		return builder.toString();
	}
}
