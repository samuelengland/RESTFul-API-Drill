package com.example.demo;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.fileUpload.StorageService;

@SpringBootApplication
public class FINRADrillApplication implements CommandLineRunner {

	@Resource
	StorageService storageService;
	
	public static void main(String[] args) {
		SpringApplication.run(FINRADrillApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		storageService.deleteAll();
		storageService.init();
	}
	
}
