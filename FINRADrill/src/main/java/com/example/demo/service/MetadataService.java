package com.example.demo.service;

import java.util.Collection;

import com.example.demo.model.Metadata;

public interface MetadataService {

	Collection<Metadata> findAll();
	
	Metadata findOne(Long id);
	
	Metadata create(Metadata metadata);
	
	Metadata update(Metadata metadata);
	
	void delete(Long id);
}
