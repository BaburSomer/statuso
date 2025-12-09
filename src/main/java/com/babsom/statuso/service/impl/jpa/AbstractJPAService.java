 package com.babsom.statuso.service.impl.jpa;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import com.babsom.statuso.model.BaseEntity;

public abstract class AbstractJPAService<TYPE extends BaseEntity, ID, REP extends CrudRepository<TYPE, ID>> {
	private final CrudRepository<TYPE, ID> repository;
	
	public AbstractJPAService(CrudRepository<TYPE, ID> repository) {
		super();
		this.repository = repository;
	}

	public Set<TYPE> findAll() {
		Set<TYPE> entities = new HashSet<>();
		repository.findAll().forEach(entities::add);
		return entities;
	}
	
	public TYPE findById(ID id) {
//		Optional<TYPE> entity = repository.findById(id);
//		return entity.isPresent() ? entity.get() : null;
//		return entity.orElse(null);
		return repository.findById(id).orElse(null);
	}
	
	public void save (TYPE object) {
		if (Objects.isNull(object)) {
			throw new RuntimeException("Object cannot be null!");
		}
		
		repository.save(object);
	}
	
	public void deleteById(ID id) {
		repository.deleteById(id);
	}
	
	public void deleteByObject (TYPE object) {
		repository.delete(object);
	}

	public CrudRepository<TYPE, ID> getRepository() {
		return repository;
	}
}
