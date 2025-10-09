package com.babsom.statuso.model;

import jakarta.persistence.Column;
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
@Table(name = "rules")
public class FactoryCreationRule extends BaseEntity{
	private static final long serialVersionUID = 8862820224402729531L;

	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String descr;
	@Column(name = "rule")
	private String rule;
	@Column(name = "importer")
	private String importerClass;
}
