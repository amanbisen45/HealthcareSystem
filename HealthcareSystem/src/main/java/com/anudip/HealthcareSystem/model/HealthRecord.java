package com.anudip.HealthcareSystem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_records")
public class HealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to the patient (from User table)
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    // Google Drive File ID
    @Column(name = "file_id", nullable = false)
    private String fileId;

    // Original name of the file
    @Column(name = "file_name", nullable = false)
    private String fileName;

    // Timestamp of upload
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    public HealthRecord() {
    }

    public HealthRecord(User patient, String fileId, String fileName, LocalDateTime uploadedAt) {
        this.patient = patient;
        this.fileId = fileId;
        this.fileName = fileName;
        this.uploadedAt = uploadedAt;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}