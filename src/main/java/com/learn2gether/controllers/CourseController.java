package com.learn2gether.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learn2gether.dao.CourseRepository;
import com.learn2gether.domain.Course;


@CrossOrigin
@RestController
public class CourseController {
	
	private final Logger logger = LoggerFactory.getLogger(CourseController.class);
	
	//Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "F://temp//";
	
	@Autowired
	private CourseRepository courseRepository;
	
	@RequestMapping(value="/courses", method=RequestMethod.POST)
	public Course createCourse(@RequestBody Course course){
		courseRepository.save(course);
		return course;
	}
	
	@RequestMapping(value="/courses", method=RequestMethod.GET)
	public  List<Course> createCourse(){
		return (List<Course>) courseRepository.findAll();
	}
	
	@RequestMapping(value = "/api/upload", headers=("content-type=multipart/*"), method = RequestMethod.POST)
     public ResponseEntity<?> uploadFile(
            @RequestParam("single") MultipartFile uploadfile) {

        logger.debug("Single file upload!");

        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        try {

            saveUploadedFiles(Arrays.asList(uploadfile));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded - " +
                uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }
	
	

	 @RequestMapping(value = "/upload", consumes = {"multipart/form-data"}, method = RequestMethod.POST)
	 public ResponseEntity<FileInfo> upload(@RequestParam("avatar") MultipartFile inputFile) {
		 logger.info("Single file upload!");
	  FileInfo fileInfo = new FileInfo();
	  HttpHeaders headers = new HttpHeaders();
	  if (!inputFile.isEmpty()) {
	   try {
	    String originalFilename = inputFile.getOriginalFilename();
	    File destinationFile = new File("F://"+  File.separator + originalFilename);
	    inputFile.transferTo(destinationFile);
	    fileInfo.setFileName(destinationFile.getPath());
	    fileInfo.setFileSize(inputFile.getSize());
	    headers.add("File Uploaded Successfully - ", originalFilename);
	    return new ResponseEntity<FileInfo>(fileInfo, headers, HttpStatus.OK);
	   } catch (Exception e) {    
	    return new ResponseEntity<FileInfo>(HttpStatus.BAD_REQUEST);
	   }
	  }else{
	   return new ResponseEntity<FileInfo>(HttpStatus.BAD_REQUEST);
	  }
	 }
	 
	 
	//save file
    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue; //next pls
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

        }

    }

}

class FileInfo {

	 private String fileName;
	 private long fileSize;

	 public String getFileName() {
	  return fileName;
	 }

	 public void setFileName(String fileName) {
	  this.fileName = fileName;
	 }

	 public long getFileSize() {
	  return fileSize;
	 }

	 public void setFileSize(long fileSize) {
	  this.fileSize = fileSize;
	 }

	}
