package com.example.account.model;


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

@Getter
@Setter
@Entity(name = "tbl_account")
public class Account extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(min = 0, max = 255)
	@Column(columnDefinition = "varchar(255) default ''", name = "first_name")
	private String firstName;
	@Size(min = 0, max = 255)
	@Column(columnDefinition = "varchar(255) default ''", name = "last_name")
	private String lastName;
	@Size(min = 2, max = 255)
	@NotNull
	@Column(columnDefinition = "varchar(255) default ''",nullable = false, unique = true)
	private String username;
	@NotNull
	@Column(columnDefinition = "varchar(500)",nullable = false)
	private String password;
	@Size(min = 2, max = 255)
	@NotNull
	@Column(columnDefinition = "varchar(255) default ''",nullable = false)
	private String role;	
}
