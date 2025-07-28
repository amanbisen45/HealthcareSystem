package com.anudip.HealthcareSystem.controller;

import com.anudip.HealthcareSystem.utility.TimeSlotUtil;
import com.anudip.HealthcareSystem.model.*;
import com.anudip.HealthcareSystem.service.AppointmentService;
import com.anudip.HealthcareSystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    // Patient Dashboard - UI Testing for Upload
    @GetMapping("/patient/dashboard")
    public String showDashboard(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("loggedInUser");
        if (user.getRole() != Role.PATIENT) {
            return "redirect:/login";
        }

        model.addAttribute("user", user); // For future use in the template
        return "patient_dashboard";
    }

    // Show book appointment form
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

        List<Doctor> doctors = userService.getAllDoctors();
        model.addAttribute("doctors", doctors);

        List<LocalTime> availableSlots = TimeSlotUtil.getAvailableSlots();
        model.addAttribute("timeSlots", availableSlots);

        return "book_appointment";
    }

    // Handling appointment booking (Also Prevents Duplicate Booking)
    @PostMapping("/patient/book-appointment")
    public String bookAppointment(@RequestParam("doctorId") Long doctorId,
                                  @RequestParam("appointmentDate") String appointmentDateStr,
                                  @RequestParam("timeSlot") String timeSlotStr,
                                  HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        User patient = (User) session.getAttribute("loggedInUser");

        // Fetch doctor details
        Doctor doctor = userService.getDoctorById(doctorId);
        if (doctor == null || doctor.getUser().getRole() != Role.DOCTOR) {
            model.addAttribute("error", "⚠️ Invalid doctor selection.");
            model.addAttribute("doctors", userService.getAllDoctors());
            model.addAttribute("timeSlots", TimeSlotUtil.getAvailableSlots());
            return "book_appointment";
        }

        try {
            LocalDate appointmentDateOnly = LocalDate.parse(appointmentDateStr);
            LocalTime timeSlot = LocalTime.parse(timeSlotStr);
            LocalDateTime appointmentDateTime = appointmentDateOnly.atStartOfDay();

            // Preventing double booking
            if (appointmentService.isSlotBooked(doctorId, appointmentDateOnly.atStartOfDay(), timeSlot)) {
                model.addAttribute("error", "⚠️ This time slot is already booked. Please choose another slot.");
                model.addAttribute("doctors", userService.getAllDoctors());
                model.addAttribute("timeSlots", TimeSlotUtil.getAvailableSlots());
                return "book_appointment";
            }

            // Book the appointment
            Appointment appointment = new Appointment();
            appointment.setPatient(patient);
            appointment.setDoctor(doctor.getUser());
            appointment.setStatus(Status.PENDING);
            appointment.setAppointmentDate(appointmentDateTime);
            appointment.setTimeSlot(timeSlot);

            appointmentService.saveAppointment(appointment);
            return "redirect:/patient/home?success=AppointmentBooked";

        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "⚠️ Slot already booked. Please choose another time.");
        } catch (Exception e) {
            model.addAttribute("error", "⚠️ An unexpected error occurred: " + e.getMessage());
        }

        // Reload doctors and available slots
        model.addAttribute("doctors", userService.getAllDoctors());
        model.addAttribute("timeSlots", TimeSlotUtil.getAvailableSlots());
        // Staying on the same page with an error
        return "book_appointment";
    }
}
