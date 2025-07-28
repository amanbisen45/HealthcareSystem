package com.anudip.HealthcareSystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "patient_doctor")
public class PatientDoctor {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "patient_id", nullable = false)
    private User patient;
	
	 @ManyToOne
	 @JoinColumn(name = "doctor_id", nullable = false)
	 private User doctor;
	 
	 public PatientDoctor() {
	    }

	    public PatientDoctor(User patient, User doctor) {
	        this.patient = patient;
	        this.doctor = doctor;
	    }

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public User getPatient() {
	        return patient;
	    }

	    public void setPatient(User patient) {
	        this.patient = patient;
	    }

	    public User getDoctor() {
	        return doctor;
	    }

	    public void setDoctor(User doctor) {
	        this.doctor = doctor;
	    }
}
