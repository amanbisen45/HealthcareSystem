package com.anudip.HealthcareSystem.repository;

import com.anudip.HealthcareSystem.model.PatientDoctor;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface PatientDoctorRepository extends JpaRepository<PatientDoctor, Long> {

    boolean existsByDoctorIdAndPatientId(Long doctorId, Long patientId);

    // ✅ Add this for doctor-patient access
    List<PatientDoctor> findByDoctorId(Long doctorId);
}
