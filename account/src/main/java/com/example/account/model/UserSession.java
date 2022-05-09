package com.example.account.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.example.common.dto.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Entity(name = "tbl_user_session")
public class UserSession extends BaseEntity {
	
	@Id
	private String token;
	
	@OneToOne
	@JoinColumn(name = "account_id")
	private Account account;
	
	@Column(name = "expire_at", insertable = true, updatable = true, columnDefinition= "TIMESTAMP WITH TIME ZONE")
	private Date expireAt;

}
