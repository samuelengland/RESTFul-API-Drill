package com.example.demo.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.model.Metadata;
import com.example.demo.repository.MetadataRepository;

@Service
public class MetaServiceBean implements MetadataService {
	
	@Autowired
	private MetadataRepository metadataRepository;
	
	@Override
	public Collection<Metadata> findAll() {
		Collection<Metadata> metadata = metadataRepository.findAll();
		return metadata;
	}

	@Override
	public Metadata findOne(Long id) {
		Metadata metadata = metadataRepository.findOne(id);
		return metadata;
	}

	@Override
	public Metadata create(Metadata metadata) {
		if(metadata.getId() != null) {
			// Cannon create Metadata with specified ID value
			return null;
		}
		
		Metadata savedMetadata = metadataRepository.save(metadata);
		return savedMetadata;
	}

	@Override
	public Metadata update(Metadata metadata) {
		Metadata metadataPersisted = findOne(metadata.getId());
		if(metadataPersisted == null) {
			// Doesn't exist so operation fails
			return null;
		}
		Metadata updatedMetadata = metadataRepository.save(metadata);
		return updatedMetadata;
	}

	@Override
	public void delete(Long id) {
		metadataRepository.delete(id);

	}

}
