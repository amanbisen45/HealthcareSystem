package com.anudip.HealthcareSystem.service;

import com.anudip.HealthcareSystem.model.HealthRecord;
import com.anudip.HealthcareSystem.repository.HealthRecordRepository;
import com.anudip.HealthcareSystem.repository.PatientDoctorRepository;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleDriveService {

    private static final String APPLICATION_NAME = "Healthcare System";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String FOLDER_ID = "10t0dORtVYJxCK7gSyv1co90uxdI6whxZ"; // HealthcareRecords folder ID

    @Autowired
    private PatientDoctorRepository patientDoctorRepository;

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    public static Drive getDriveService() throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        InputStream in = GoogleDriveService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleCredentials credentials = GoogleCredentials.fromStream(in).createScoped(SCOPES);
        return new Drive.Builder(httpTransport, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    // ✅ Updated: Only returns the Google Drive File ID now
    public String uploadFile(MultipartFile file, Long patientId) {
        try {
            Drive service = getDriveService();

            File fileMetadata = new File();
            fileMetadata.setName(file.getOriginalFilename());
            fileMetadata.setParents(Collections.singletonList(FOLDER_ID));
            fileMetadata.setDescription("Patient ID: " + patientId);

            java.io.File tempFile = convertMultipartFileToFile(file);
            FileContent mediaContent = new FileContent(file.getContentType(), tempFile);

            File uploadedFile = service.files().create(fileMetadata, mediaContent)
                    .setFields("id, name")
                    .execute();

            tempFile.delete();

            return uploadedFile.getId(); // ✅ Return only the File ID, not full description
        } catch (Exception e) {
            e.printStackTrace();
            return "Error uploading file: " + e.getMessage();
        }
    }

    public ResponseEntity<byte[]> getFile(String fileId, String userRole, Long userId, Long patientId) {
        try {
            // RBAC check
            if ("DOCTOR".equals(userRole)) {
                if (!hasDoctorAccessToRecord(userId, patientId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
            } else if (!"ADMIN".equals(userRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            // Fetch filename from database
            String filename = healthRecordRepository.findByFileId(fileId)
                    .map(HealthRecord::getFileName)
                    .orElse("record");

            // Download from Drive
            Drive service = getDriveService();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            service.files().get(fileId).executeMediaAndDownloadTo(outputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private boolean hasDoctorAccessToRecord(Long doctorId, Long patientId) {
        return patientDoctorRepository.existsByDoctorIdAndPatientId(doctorId, patientId);
    }

    private java.io.File convertMultipartFileToFile(MultipartFile file) throws IOException {
        java.io.File convFile = new java.io.File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    public List<String> getFilesForPatient(Long patientId, String userRole, Long userId) {
        List<String> downloadLinks = new ArrayList<>();
        try {
            if ("DOCTOR".equals(userRole)) {
                if (!hasDoctorAccessToRecord(userId, patientId)) {
                    return Collections.singletonList("Access Denied: You do not have permission to view this patient's records.");
                }
            } else if (!"ADMIN".equals(userRole)) {
                return Collections.singletonList("Access Denied: Insufficient permissions.");
            }

            Drive service = getDriveService();
            FileList result = service.files().list()
                    .setQ("'" + FOLDER_ID + "' in parents and name contains 'patient_" + patientId + "_'")
                    .setFields("files(id, name)")
                    .execute();

            if (result.getFiles().isEmpty()) {
                return Collections.singletonList("No records found for this patient.");
            }

            for (File file : result.getFiles()) {
                String downloadLink = "https://drive.google.com/uc?id=" + file.getId();
                downloadLinks.add(downloadLink);
            }

        } catch (Exception e) {
            e.printStackTrace();
            downloadLinks.add("Error retrieving records: " + e.getMessage());
        }
        return downloadLinks;
    }
}

