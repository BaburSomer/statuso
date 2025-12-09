package com.babsom.statuso.model;

import java.io.Serializable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity implements Serializable {
	private static final long serialVersionUID = 428751301046498516L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long oid;
	private Long mandt;
}