package com.anudip.HealthcareSystem.controller;

import com.anudip.HealthcareSystem.model.Appointment;
import com.anudip.HealthcareSystem.model.Status;
import com.anudip.HealthcareSystem.model.User;
import com.anudip.HealthcareSystem.repository.AppointmentRepository;
import com.anudip.HealthcareSystem.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AppointmentRepository appointmentRepository;


    // Get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    // Delete user and redirect to dashboard
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return "redirect:/admin/dashboard";
    }


    // Update user information
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return adminService.updateUser(id, user);
    }

    // Get all appointments
    @GetMapping("/appointments")
    public List<Appointment> getAllAppointments() {
        return adminService.getAllAppointments();
    }


    // Delete an appointment and redirect to dashboard
    @DeleteMapping("/appointments/{id}")
    public String deleteAppointment(@PathVariable Long id, Model model) {
        adminService.deleteAppointment(id);
        return "redirect:/admin/dashboard";
    }

    //Update Status of =appointment
    @PutMapping("/appointments/{id}/status")
    public String updateAppointmentStatus(@PathVariable Long id, @RequestParam("status") String status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(Status.valueOf(status));
        appointmentRepository.save(appointment);

        return "redirect:/admin/dashboard";
    }


    //Admin Dashboard
    @GetMapping("/dashboard")
    public String adminDashboard(Model model, HttpSession session) {
        // Check if user is logged in
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        // Fetch users and appointments
        List<User> users = adminService.getAllUsers();
        List<Appointment> appointments = adminService.getAllAppointments();

        // Add data to model
        model.addAttribute("users", users);
        model.addAttribute("appointments", appointments);

        return "admin_dashboard";
    }

}

