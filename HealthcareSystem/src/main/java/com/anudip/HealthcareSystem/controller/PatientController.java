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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PatientController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    // 📌 Patient Home Page
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

    // 📌 Show book appointment form
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


    // 📌 Handle appointment booking
    @PostMapping("/book-appointment")
    public String bookAppointment(HttpServletRequest request, @ModelAttribute Appointment appointment, @RequestParam Long doctorId) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        User patient = (User) session.getAttribute("loggedInUser");
        if (patient.getRole() != Role.PATIENT) {
            return "redirect:/login";
        }

        User doctor = userService.getUserById(doctorId);
        if (doctor == null || doctor.getRole() != Role.DOCTOR) {
            return "redirect:/book-appointment?error=InvalidDoctor";
        }

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(LocalDateTime.now().plusDays(1));  // Default to next day
        appointment.setStatus(Status.PENDING);  // Ensure Status enum has PENDING

        appointmentService.saveAppointment(appointment);

        return "redirect:/patient/home";  // Redirect to patient home
    }
}
