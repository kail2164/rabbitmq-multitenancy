package com.example.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseProduct extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8319664004998508778L;	
	private Long id;
	private String name;
	private Double price;
	private String imgUrl;
}
