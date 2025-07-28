package com.anudip.HealthcareSystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.anudip.HealthcareSystem.service.HealthcareRecordsService;

@RestController
@RequestMapping("/api/healthcare-records")
public class HealthcareRecordsController {
	
	private final HealthcareRecordsService healthcareRecordsService;

    public HealthcareRecordsController(HealthcareRecordsService healthcareRecordService) {
        this.healthcareRecordsService = healthcareRecordService;
    }

    // Check if a doctor has access to a patient's healthcare records
    @GetMapping("/access")
    public ResponseEntity<Boolean> hasAccess(
    		@RequestParam(name = "doctorId", required = false) Long doctorId, 
    		@RequestParam(name = "patientId", required = false) Long patientId) {
    	
    	System.out.println("Received doctorId: " + doctorId + ", patientId: " + patientId);
    	
    	if(doctorId == null || patientId == null) {
    		return ResponseEntity.badRequest().body(false);  // Ensures a response
    	}
        boolean access = healthcareRecordsService.hasDoctorAccessToRecord(doctorId, patientId);
        return ResponseEntity.ok(access);
    }

}
