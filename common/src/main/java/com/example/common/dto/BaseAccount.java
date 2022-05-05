package com.example.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseAccount extends BaseDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7550047160520896434L;
	private Long id;
	private String firstName;
	private String lastName;
	private String username;
}
