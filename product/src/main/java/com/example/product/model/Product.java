package com.example.product.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.common.dto.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "tbl_product")
@Getter
@Setter
public class Product extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(min = 2, max = 255)
	@NotNull
	@Column(columnDefinition = "varchar(255) default ''",nullable = false)
	private String name;
	private Double price;
	@Size(min = 2, max = 255)
	@NotNull
	@Column(columnDefinition = "varchar(255) default ''",nullable = false, name = "img_url")
	private String imgUrl;	
}
