package com.example.demo.fileUpload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.example.demo.model.Metadata;
import com.example.demo.service.MetadataService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;


@Controller
public class FileUploadController {
	 
    @Autowired
    StorageService storageService;
    
	@Autowired
	private MetadataService metadataService;
 
    HashSet<String> files = new HashSet<String>();
 
    @GetMapping("/")
    public String listUploadedFiles(Model model) {
        return "uploadForm";
    }
 
    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
    	
    	MultipartFile newFile = file;
    	// Attempt to read and save the file
    	try {

        	// Convert file and check if you can parse the json... there is a better way to do this somehow
        	try {
        		File convertedFile = convertToFile(file);
        	
        		// Read the json file in
        		final Type METADATA_TYPE = new TypeToken<List<Metadata>>() {}.getType();
	        	Gson gson = new Gson();
	        	JsonReader reader = new JsonReader(new FileReader(convertedFile));
	        	List<Metadata> data = gson.fromJson(reader, METADATA_TYPE);
	        	
	        	// Save the new data to the database
	        	for(Metadata metadata  : data) {
	        		Metadata savedMetadata = metadataService.create(metadata);
	        	}
        	
        	} catch (Exception e) {
        		throw new Exception();
        	}
        	
        	// Grab the updated data from the DB and write to file... dirty way of doing this
        	List<Metadata> metadata = (List<Metadata>) metadataService.findAll();
        	String fileContents = new Gson().toJson(metadata);
        	
        	File tempFile = new File("metadata.txt");
        	tempFile.createNewFile(); // if file already exists will do nothing 
        	FileOutputStream oFile = new FileOutputStream(tempFile, false);
        	oFile.write(fileContents.getBytes());
        	oFile.close();
        	
        	newFile = convertToMulti(tempFile);
        	
        	// Clean the service and then store the updated file
        	storageService.store(newFile);
        	
            model.addAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
            files.add(newFile.getOriginalFilename());
        } catch (Exception e) {
            model.addAttribute("message", "FAIL to upload " + file.getOriginalFilename() + "!");
        }
        return "uploadForm";
    }
 
    @GetMapping("/getallfiles")
    public String getListFiles(Model model) {	
    	
    	// Shows the files and downloads, the last up to date file
        model.addAttribute("files",
                files.stream()
                        .map(fileName -> MvcUriComponentsBuilder
                                .fromMethodName(FileUploadController.class, "getFile", fileName).build().toString())
                        .collect(Collectors.toList()));
        model.addAttribute("totalFiles", "TotalFiles: " + files.size());
        return "listFiles";
    }
 
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
    
    
    // ====================HELPERS=======================
    
    public File convertToFile(MultipartFile file) throws IOException {    
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile(); 
        FileOutputStream fos = new FileOutputStream(convFile); 
        fos.write(file.getBytes());
        fos.close(); 
        return convFile;
    }
    
    public MultipartFile convertToMulti(File file) throws IOException {
    	FileInputStream input = new FileInputStream(file);
    	MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/json", IOUtils.toByteArray(input));
    	return multipartFile;
    }
    
}
