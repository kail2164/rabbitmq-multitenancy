package com.example.common.dto;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseEntity {
	@Column(name = "create_at", insertable = true, updatable = false, columnDefinition= "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
	private OffsetDateTime createAt;
	
	@Column(name = "update_at", insertable = true, updatable = true, columnDefinition= "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
	private OffsetDateTime updateAt;	
	
	@PrePersist
	public void onCreate() {
		this.setCreateAt(OffsetDateTime.now());
		this.setUpdateAt(OffsetDateTime.now());		
	}

	@PreUpdate
	public void onPersist() {
		this.setUpdateAt(OffsetDateTime.now());		
	}
}
