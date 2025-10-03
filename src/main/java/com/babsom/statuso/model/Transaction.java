package com.babsom.statuso.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Entity;
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
@Table(name = "transactions")

public class Transaction extends BaseEntity {

	private static final long serialVersionUID = -30397773786376571L;

	private Date            date;
	private Time            time;
	private String          reference;
	private BigDecimal      amount;
	private TransactionType sortOfTransaction;
	private String          description;
}
