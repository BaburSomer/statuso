package com.babsom.statuso.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "currency_rates")
public class CurrencyRate implements Serializable{
	private static final long serialVersionUID = -2003792545671271214L;
	
	@Id
	@Column(name = "date", length = 10, unique = true)
	private String date;
	@Column(nullable = true, precision = 7, scale = 5)
	private BigDecimal dollar;
	@Column(nullable = true, precision = 7, scale = 5)
	private BigDecimal euro;
	@Column(nullable = true, precision = 7, scale = 5)
	private BigDecimal chf;
	@Column(nullable = true, precision = 7, scale = 5)
	private BigDecimal gbp;
	
	public CurrencyRate(String date, BigDecimal euro, BigDecimal dollar, BigDecimal chf, BigDecimal gbp) {
		super();
		this.date   = date;
		this.dollar = dollar;
		this.euro   = euro;
		this.chf    = chf;
		this.gbp    = gbp;
	}
}
