package com.anudip.HealthcareSystem.controller;

import com.anudip.HealthcareSystem.model.Appointment;
import com.anudip.HealthcareSystem.model.Role;
import com.anudip.HealthcareSystem.model.Status;
import com.anudip.HealthcareSystem.model.User;
import com.anudip.HealthcareSystem.service.AppointmentService;
import com.anudip.HealthcareSystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PatientController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    // Patient Home Page
    @GetMapping("/patient/home")
    public String patientHome(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("loggedInUser");
        if (user.getRole() != Role.PATIENT) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "patient_home";
    }

    //Show book appointment form
    @GetMapping("/patient/book-appointment")
    public String showBookAppointmentForm(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("loggedInUser");
        if (user.getRole() != Role.PATIENT) {
            return "redirect:/login";
        }

        model.addAttribute("doctors", userService.getAllDoctors());
        return "book_appointment";
    }

    // Handle appointment booking (Also Prevents Duplicate Booking)
    @PostMapping("/patient/book-appointment")
    public String bookAppointment(@RequestParam("doctorId") Long doctorId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        User patient = (User) session.getAttribute("loggedInUser");
        User doctor = userService.getUserById(doctorId);

        if (doctor == null || doctor.getRole() != Role.DOCTOR) {
            return "redirect:/patient/book-appointment?error=InvalidDoctor";
        }

        // Checking if the patient already has an ACTIVE appointment with the same doctor
        boolean exists = appointmentService.existsByPatientIdAndDoctorIdAndStatus(patient.getId(), doctorId);
        if (exists) {
            model.addAttribute("doctors", userService.getAllDoctors());
            model.addAttribute("error", "You already have a pending or accepted appointment with this doctor.");
            return "book_appointment";
        }

        //If no active appointment exists, proceed with booking
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setStatus(Status.PENDING);
        appointment.setAppointmentDate(LocalDateTime.now());

        appointmentService.saveAppointment(appointment);

        return "redirect:/patient/home?success=AppointmentBooked";
    }

}
