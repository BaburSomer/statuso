package com.babsom.statuso.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "transactions", indexes = { @Index(name = "pk_index_2", columnList = "mandt, oid", unique = true),
		@Index(name = "ak_index", columnList = "source, timestamp DESC", unique = false),
		@Index(name = "ak2_index", columnList = "mandt, source, timestamp, amount", unique = true) })
public class Transaction extends BaseEntity {

	private static final long serialVersionUID = -30397773786376571L;

	@Column(nullable = false, unique = false)
	private Timestamp timestamp;

	@Column(nullable = true, unique = false)
	private String reference;

	@Column(nullable = false, precision = 11, scale = 2)
	private BigDecimal amount;

	@Column(nullable = false)
	private TransactionDirection direction;

	@Column(nullable = true)
	private TransactionClass grouping;

	private String description;
	
	@Column(nullable = true, length = 30)
	private String type;

	@Column(nullable = false, length = 30)
	private String source;

	@Column(nullable = true, precision = 7, scale = 2)
	private BigDecimal originalAmount;

	@Column(nullable = true, precision = 5, scale = 2)
	private BigDecimal exchangeRate;
	
	@Column(nullable = true, length = 3)
	private String currency;

}
