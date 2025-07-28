package com.anudip.HealthcareSystem.service;

import com.anudip.HealthcareSystem.model.Appointment;
import com.anudip.HealthcareSystem.model.Status;
import com.anudip.HealthcareSystem.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public boolean existsByDoctorIdAndTimeSlot(Long doctorId, LocalTime timeSlot) {
        return appointmentRepository.existsByDoctorIdAndTimeSlotAndStatus(doctorId, timeSlot, Status.PENDING);
    }


    public boolean isSlotBooked(Long doctorId, LocalDateTime appointmentDateTime, LocalTime timeSlot) {
        return appointmentRepository.existsByDoctorIdAndAppointmentDateAndTimeSlot(
                doctorId, appointmentDateTime, timeSlot
        );
    }



}
