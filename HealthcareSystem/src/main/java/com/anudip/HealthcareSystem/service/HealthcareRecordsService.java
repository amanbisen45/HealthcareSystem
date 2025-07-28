package com.anudip.HealthcareSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anudip.HealthcareSystem.repository.AppointmentRepository;

@Service
public class HealthcareRecordsService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Check if a doctor has access to a patient's records
    public boolean hasDoctorAccessToRecord(Long doctorId, Long patientId) {
        List<Long> appointments = appointmentRepository.findApprovedAppointments(doctorId, patientId);
        System.out.println("Appointments found: " + appointments);
        return !appointments.isEmpty();
    }
}
