package com.anudip.HealthcareSystem.controller;

import com.anudip.HealthcareSystem.model.HealthRecord;
import com.anudip.HealthcareSystem.model.Role;
import com.anudip.HealthcareSystem.model.User;
import com.anudip.HealthcareSystem.service.AppointmentService;
import com.anudip.HealthcareSystem.service.HealthRecordService;
import com.anudip.HealthcareSystem.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class DoctorController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private HealthRecordService healthRecordService;

    @Autowired
    private UserService userService;

    // ✅ Doctor Dashboard
    @GetMapping("/doctor/home")
    public ModelAndView doctorDashboard(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || user.getRole() != Role.DOCTOR) {
            return new ModelAndView("redirect:/login");
        }

        ModelAndView mav = new ModelAndView("doctor_home");
        mav.addObject("user", user);
        mav.addObject("appointments", appointmentService.getAppointmentsByDoctorId(user.getId()));
        mav.addObject("patients", appointmentService.getPatientsForDoctor(user.getId()));
        return mav;
    }

    // ✅ View Patient Records (Access-Controlled)
    @GetMapping("/doctor/view-records")
    public ModelAndView viewPatientRecords(@RequestParam("patientId") Long patientId, HttpSession session) {
        User doctor = (User) session.getAttribute("loggedInUser");

        if (doctor == null || doctor.getRole() != Role.DOCTOR) {
            return new ModelAndView("redirect:/login");
        }

        boolean allowed = appointmentService.hasApprovedAppointmentWithPatient(doctor.getId(), patientId);
        if (!allowed) {
            return new ModelAndView("error/unauthorized");
        }

        List<HealthRecord> records = healthRecordService.getRecordsByPatientId(patientId);
        User patient = userService.getUserById(patientId); // for display in template

        ModelAndView mav = new ModelAndView("view_patient_records");
        mav.addObject("records", records);
        mav.addObject("patientId", patientId);
        mav.addObject("patient", patient);
        return mav;
    }
}
