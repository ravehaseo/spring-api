package com.maybank.sample.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.sun.istack.NotNull;

@MappedSuperclass
public abstract class BasicTable implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private final Timestamp createdDate = Timestamp.from(Instant.now());

	@UpdateTimestamp
	@Column(insertable = false)
	private Timestamp modifiedDate;

	public Long getId() {
		return id;
	}

	public Instant getCreatedInstant() {
		return createdDate == null ? null : createdDate.toInstant();
	}

	public Date getCreatedDate() {
		return createdDate == null ? null : Date.from(getCreatedInstant());
	}

	public Instant getModifiedInstant() {
		return modifiedDate == null ? null : modifiedDate.toInstant();
	}

	public Date getModifiedDate() {
		return modifiedDate == null ? null : Date.from(getModifiedInstant());
	}

}
