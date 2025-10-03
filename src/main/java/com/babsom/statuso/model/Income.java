package com.babsom.statuso.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class Income extends Transaction{
	private static final long serialVersionUID = 9183368665366507422L;
	
	private IncomeType type;
}
