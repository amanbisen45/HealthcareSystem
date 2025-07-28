package com.anudip.HealthcareSystem.repository;

import com.anudip.HealthcareSystem.model.Appointment;
import com.anudip.HealthcareSystem.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    boolean existsByDoctorIdAndTimeSlotAndStatus(Long doctorId, LocalTime timeSlot, Status status);

    boolean existsByDoctorIdAndAppointmentDateAndTimeSlot(Long doctorId, LocalDateTime appointmentDate, LocalTime timeSlot);

    // 🔍 Check if doctor has approved appointment with specific patient
    @Query("SELECT a.id FROM Appointment a WHERE a.doctor.id = :doctorId AND a.patient.id = :patientId AND a.status = 'APPROVED'")
    List<Long> findApprovedAppointments(
            @Param("doctorId") Long doctorId,
            @Param("patientId") Long patientId
    );

    // ✅ Get all unique patient IDs for approved appointments for a doctor
    @Query("SELECT DISTINCT a.patient.id FROM Appointment a WHERE a.doctor.id = :doctorId AND a.status = 'APPROVED'")
    List<Long> findApprovedAppointments(@Param("doctorId") Long doctorId);
}
