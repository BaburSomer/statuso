package com.babsom.statuso.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inflation_rates")
public class AnnualInflationRate implements Serializable{
	private static final long serialVersionUID = -2003792545671271214L;
	
	@Id
	@Column(name = "inflation_year", nullable = false, unique = true)
	private Integer year;
	@Column(nullable = true, precision = 5, scale = 2)
	private BigDecimal turkey;
	@Column(nullable = true, precision = 5, scale = 2)
	private BigDecimal europe;
	@Column(nullable = true, precision = 5, scale = 2)
	private BigDecimal usa;
	@Column(nullable = true, precision = 5, scale = 2)
	private BigDecimal switzerland;
	@Column(nullable = true, precision = 5, scale = 2)
	private BigDecimal britain;
	
}
