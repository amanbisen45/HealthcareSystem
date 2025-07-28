package com.anudip.HealthcareSystem.repository;

import com.anudip.HealthcareSystem.model.HealthRecord;
import com.anudip.HealthcareSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {

    List<HealthRecord> findByPatient(User patient);

    Optional<HealthRecord> findByFileId(String fileId);
}
