package com.anudip.HealthcareSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.anudip.HealthcareSystem.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {
	 @Autowired
	    private AppointmentService appointmentService;

	    @PostMapping("/approve/{appointmentId}")
	    public ResponseEntity<String> approveAppointment(@PathVariable Long appointmentId) {
	        appointmentService.approveAppointment(appointmentId);
	        return ResponseEntity.ok("Appointment approved and doctor-patient relationship added.");
	    }

}
