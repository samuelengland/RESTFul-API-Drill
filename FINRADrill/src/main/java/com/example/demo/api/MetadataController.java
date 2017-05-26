package com.example.demo.api;


import java.util.Collection;

import com.example.demo.model.Metadata;
import com.example.demo.service.MetadataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetadataController {
	
	
	@Autowired
	private MetadataService metadataService;
	
	// A way to find all 
	@RequestMapping(
			value="/api/metadata",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Metadata>> getMetadata() {
		
		Collection<Metadata> metadata = metadataService.findAll();
		
		return new ResponseEntity<Collection<Metadata>>(metadata, HttpStatus.OK);
		
	}
	
	// A way to find specific through id
	@RequestMapping(
			value="/api/metadata/{id}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Metadata> getMetadata(@PathVariable("id") Long id) {
		
		Metadata metadata =  metadataService.findOne(id);
		if(metadata == null) {
			return new ResponseEntity<Metadata>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Metadata>(metadata, HttpStatus.OK);
	}
	
	// A way to create
	@RequestMapping(
			value="/api/metadata", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Metadata> createMetadata(@RequestBody Metadata metadata) {
		
		Metadata savedMetadata = metadataService.create(metadata);
		return new ResponseEntity<Metadata>(savedMetadata, HttpStatus.CREATED);
	}
	
	// A way to update
	@RequestMapping(
			value="/api/metadata/{id}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Metadata> updateMetadata(@RequestBody Metadata metadata) {
		
		Metadata updatedMetadata = metadataService.update(metadata);
		if(updatedMetadata == null) {
			return new ResponseEntity<Metadata>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Metadata>(updatedMetadata, HttpStatus.OK);
	}

	// A way to delete
	@RequestMapping(
			value="/api/metadata/{id}",
			method = RequestMethod.DELETE)
	public ResponseEntity<Metadata> deleteMetadata(@PathVariable("id") Long id) {
		
		metadataService.delete(id);
		
		return new ResponseEntity<Metadata>(HttpStatus.NO_CONTENT);
	}
}
