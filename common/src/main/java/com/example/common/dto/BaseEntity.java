package com.example.common.dto;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity {
	@Column(name = "create_at", insertable = true, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private OffsetDateTime createAt;

	@Column(name = "modified_at", insertable = true, updatable = true, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private OffsetDateTime modifiedAt;

	@Column(name = "created_by", insertable = true, updatable = false, columnDefinition = "VARCHAR(255)")
	private String createdBy;

	@Column(name = "modified_by", insertable = true, updatable = true, columnDefinition = "VARCHAR(255)")
	private String modifiedBy;

	@PrePersist
	private void onPrePersist() {
		OffsetDateTime now = OffsetDateTime.now();
		this.setCreateAt(now);
		this.setModifiedAt(now);
		setUserAudit(true);
	}

	@PreUpdate
	private void onPreUpdate() {
		this.setModifiedAt(OffsetDateTime.now());
		setUserAudit(false);
	}

	private void setUserAudit(boolean isCreate) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			String username;
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else {
				username = principal.toString();
			}
			if (isCreate)
				this.setCreatedBy(username);
			this.setModifiedBy(username);
		}
	}
}
