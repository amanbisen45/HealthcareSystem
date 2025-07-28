package com.anudip.HealthcareSystem.service;

import com.anudip.HealthcareSystem.model.HealthRecord;
import com.anudip.HealthcareSystem.model.User;
import com.anudip.HealthcareSystem.repository.HealthRecordRepository;
import com.anudip.HealthcareSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HealthRecordService {

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Save a new health record
    public HealthRecord saveHealthRecord(User patient, String fileId, String fileName) {
        HealthRecord record = new HealthRecord(patient, fileId, fileName, LocalDateTime.now());
        return healthRecordRepository.save(record);
    }

    // ✅ Get all records for a patient (using User object)
    public List<HealthRecord> getRecordsByPatient(User patient) {
        return healthRecordRepository.findByPatient(patient);
    }

    // ✅ Get all records for a patient (using ID)
    public List<HealthRecord> getRecordsByPatientId(Long patientId) {
        return userRepository.findById(patientId)
                .map(this::getRecordsByPatient)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + patientId));
    }

    // ✅ Find a record by file ID
    public HealthRecord getRecordByFileId(String fileId) {
        return healthRecordRepository.findByFileId(fileId).orElse(null);
    }

    // ✅ Get a health record by record ID (used for download)
    public HealthRecord getRecordById(Long recordId) {
        return healthRecordRepository.findById(recordId).orElse(null);
    }

    // ✅ NEW: Get all records (for admin)
    public List<HealthRecord> getAllRecords() {
        return healthRecordRepository.findAll();
    }
    
    
}
