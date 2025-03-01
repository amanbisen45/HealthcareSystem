package com.anudip.HealthcareSystem.service;

import com.anudip.HealthcareSystem.model.Appointment;
import com.anudip.HealthcareSystem.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public void bookAppointment(Appointment appointment) {
        appointment.setStatus("Pending");
        appointmentRepository.save(appointment);
    }
}
