package com.anudip.HealthcareSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.anudip.HealthcareSystem.service.GoogleDriveService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/drive")
public class GoogleDriveController {
	
	@Autowired
	private GoogleDriveService googleDriveService;
	
	//Upload a Healthcare record
	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("patientId") Long patientId){
		if(file==null|| file.isEmpty()) {
			return ResponseEntity.badRequest().body("File is missing!");
		}
		String response = googleDriveService.uploadFile(file, patientId);
		return ResponseEntity.ok(response);
		
	}
	
	//Retrieve a File with Access Control
	@GetMapping("/file/{fileId}")
	public ResponseEntity <byte[]> getFile(
			@PathVariable String fileId,
            @RequestParam("userRole") String userRole,
            @RequestParam("userId") Long userId,
            @RequestParam("patientId") Long patientId){
		 return googleDriveService.getFile(fileId, userRole, userId, patientId);
	}
	
	@GetMapping("/files")
	public ResponseEntity<List<String>> getPatientFiles(
	        @RequestParam Long patientId,
	        @RequestParam String userRole,
	        @RequestParam Long userId) {

	    List<String> fileLinks = googleDriveService.getFilesForPatient(patientId, userRole, userId);
	    return ResponseEntity.ok(fileLinks);
	}

}
