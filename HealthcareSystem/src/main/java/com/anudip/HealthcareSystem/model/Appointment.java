package com.anudip.HealthcareSystem.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "appointment", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"doctor_id", "appointmentDate", "timeSlot"})
})
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;  // Patient who booked the appointment

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;  // Doctor assigned to the appointment

    @Column(nullable = false)
    private LocalTime timeSlot;


    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    // Constructors
    public Appointment() {}

    public Appointment(User patient, User doctor, LocalDateTime appointmentDate, Status status) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getPatient() { return patient; }
    public void setPatient(User patient) { this.patient = patient; }

    public User getDoctor() { return doctor; }
    public void setDoctor(User doctor) { this.doctor = doctor; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalTime getTimeSlot() { return timeSlot; }
    public void setTimeSlot(LocalTime timeSlot) { this.timeSlot = timeSlot; }
}

