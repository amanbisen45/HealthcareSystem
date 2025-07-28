package com.anudip.HealthcareSystem.service;

import com.anudip.HealthcareSystem.model.Appointment;
import com.anudip.HealthcareSystem.model.Status;
import com.anudip.HealthcareSystem.model.User;
import com.anudip.HealthcareSystem.repository.AppointmentRepository;
import com.anudip.HealthcareSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete user by ID
    public String deleteUser(Long id) {
        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    // Update user details
    public User updateUser(Long id, User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmail(userDetails.getEmail());
            user.setRole(userDetails.getRole());
            return userRepository.save(user);
        }
        return null;
    }

    // Get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Delete an appointment
    public String deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
        return "Appointment deleted successfully";
    }

    // Update appointment status with ENUM
    public Appointment updateAppointmentStatus(Long id, Status status) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setStatus(status);
            return appointmentRepository.save(appointment);
        }
        return null;
    }
}