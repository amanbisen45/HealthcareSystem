package com.anudip.HealthcareSystem.repository;

import com.anudip.HealthcareSystem.model.Appointment;
import com.anudip.HealthcareSystem.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorId(Long doctorId);

    boolean existsByPatientIdAndDoctorIdAndStatusIn(Long patientId, Long doctorId, List<Status> pending);
}
