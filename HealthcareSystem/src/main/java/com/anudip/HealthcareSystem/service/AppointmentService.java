package com.anudip.HealthcareSystem.service;

import com.anudip.HealthcareSystem.model.Appointment;
import com.anudip.HealthcareSystem.model.PatientDoctor;
import com.anudip.HealthcareSystem.model.Status;
import com.anudip.HealthcareSystem.model.User;
import com.anudip.HealthcareSystem.repository.AppointmentRepository;
import com.anudip.HealthcareSystem.repository.PatientDoctorRepository;
import com.anudip.HealthcareSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientDoctorRepository patientDoctorRepository;

    @Autowired
    private UserService userService;

    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public boolean bookAppointment(Appointment appointment) {
        boolean slotAlreadyBooked = isSlotBooked(
                appointment.getDoctor().getId(),
                appointment.getAppointmentDate(),
                appointment.getTimeSlot()
        );

        if (slotAlreadyBooked) {
            return false;
        }

        appointment.setStatus(Status.PENDING);

        try {
            appointmentRepository.save(appointment);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public boolean existsByDoctorIdAndTimeSlot(Long doctorId, LocalTime timeSlot, Status status) {
        return appointmentRepository.existsByDoctorIdAndTimeSlotAndStatus(doctorId, timeSlot, status);
    }

    public boolean isSlotBooked(Long doctorId, LocalDateTime appointmentDateTime, LocalTime timeSlot) {
        return appointmentRepository.existsByDoctorIdAndAppointmentDateAndTimeSlot(
                doctorId, appointmentDateTime, timeSlot
        );
    }

    public void approveAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getDoctor() == null || appointment.getPatient() == null) return;

        appointment.setStatus(Status.APPROVED);
        appointmentRepository.save(appointment);

        boolean exists = patientDoctorRepository.existsByDoctorIdAndPatientId(
                appointment.getDoctor().getId(),
                appointment.getPatient().getId()
        );

        if (!exists) {
            PatientDoctor patientDoctor = new PatientDoctor(appointment.getPatient(), appointment.getDoctor());
            patientDoctorRepository.save(patientDoctor);
        }
    }

    // ✅ Return full User objects for patients with approved appointments
    public List<User> getPatientsForDoctor(Long doctorId) {
        List<Long> patientIds = appointmentRepository.findApprovedAppointments(doctorId);
        return patientIds.stream()
                .map(userService::getUserById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // ✅ Used in DoctorController to verify access to records
    public boolean hasApprovedAppointmentWithPatient(Long doctorId, Long patientId) {
        return appointmentRepository.findApprovedAppointments(doctorId, patientId).size() > 0;
    }
}
