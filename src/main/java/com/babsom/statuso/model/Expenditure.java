package com.babsom.statuso.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class Expenditure extends Transaction{
	private static final long serialVersionUID = -372821846482797877L;
	
	private ExpenditureType type;
}
