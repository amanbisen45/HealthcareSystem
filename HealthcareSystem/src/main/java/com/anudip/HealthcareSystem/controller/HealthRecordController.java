package com.anudip.HealthcareSystem.controller;

import com.anudip.HealthcareSystem.model.HealthRecord;
import com.anudip.HealthcareSystem.model.User;
import com.anudip.HealthcareSystem.repository.UserRepository;
import com.anudip.HealthcareSystem.service.GoogleDriveService;
import com.anudip.HealthcareSystem.service.HealthRecordService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/health-records")
public class HealthRecordController {

    @Autowired
    private GoogleDriveService googleDriveService;

    @Autowired
    private HealthRecordService healthRecordService;

    @Autowired
    private UserRepository userRepository;

    // ✅ Upload via web or Postman
    @PostMapping("/upload")
    public ResponseEntity<String> uploadHealthRecord(@RequestParam("file") MultipartFile file,
                                                     @RequestHeader(value = "X-USER-EMAIL", required = false) String userEmail,
                                                     HttpServletRequest request) {
        User user = null;

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedInUser") != null) {
            user = (User) session.getAttribute("loggedInUser");
        }

        if (user == null && userEmail != null) {
            user = userRepository.findByEmail(userEmail).orElse(null);
        }

        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid user.");
        }

        try {
            String fileId = googleDriveService.uploadFile(file, user.getId());
            healthRecordService.saveHealthRecord(user, fileId, file.getOriginalFilename());
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    // ✅ Patient views their own records
    @GetMapping
    public ResponseEntity<List<HealthRecord>> getMyRecords(@RequestHeader("X-USER-EMAIL") String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        List<HealthRecord> records = healthRecordService.getRecordsByPatient(user);
        return ResponseEntity.ok(records);
    }

    // ✅ Admin/Doctor view patient records
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPatientRecords(@PathVariable Long patientId,
                                               @RequestHeader("X-USER-EMAIL") String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String role = user.getRole().name();
        Long userId = user.getId();

        List<String> links = googleDriveService.getFilesForPatient(patientId, role, userId);
        return ResponseEntity.ok(links);
    }

    // ✅ Download a specific health record (admin/doctor)
    @GetMapping("/download/{recordId}")
    public ResponseEntity<?> downloadFile(@PathVariable Long recordId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        HealthRecord record = healthRecordService.getRecordById(recordId);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }

        return googleDriveService.getFile(
                record.getFileId(),
                loggedInUser.getRole().name(),
                loggedInUser.getId(),
                record.getPatient().getId()
        );
    }
}
