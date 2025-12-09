package com.babsom.statuso.model;

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
@Table(name = "rules", indexes = { @Index(name = "pk_index", columnList = "mandt, oid", unique = true) })
public class FactoryCreationRule extends BaseEntity {

	private static final long serialVersionUID = 8862820224402729531L;

	@Column(name = "name", nullable = false, length = 30, unique = true)
	private String    name;

	@Column(name = "description", nullable = true)
	private String    descr;

	@Column(name = "file_class", nullable = false)
	private FileClass fileClass;

	@Column(name = "rule", nullable = false, length = 50)
	private String    rule;

	@Column(name = "importer", nullable = false, length = 50)
	private String    importerClass;
}
