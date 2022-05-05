package com.example.common.dto;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
	@Column(name = "create_at", insertable = true, updatable = false, columnDefinition= "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
	@CreatedDate
	private OffsetDateTime createAt;
	
	@Column(name = "modified_at", insertable = true, updatable = true, columnDefinition= "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
	@LastModifiedDate
	private OffsetDateTime modifiedAt;	
	
	@Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "modified_by")
    @LastModifiedBy
    private String modifiedBy;
}
