package com.anudip.HealthcareSystem.controller;

import com.anudip.HealthcareSystem.model.Appointment;
import com.anudip.HealthcareSystem.model.Role;
import com.anudip.HealthcareSystem.model.Status;
import com.anudip.HealthcareSystem.model.User;
import com.anudip.HealthcareSystem.service.AppointmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // 📌 Show Patient's Appointments
    @GetMapping("/appointments")
    public ModelAndView patientAppointments(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return new ModelAndView("redirect:/login");
        }

        User user = (User) session.getAttribute("loggedInUser");
        if (user.getRole() != Role.PATIENT) {
            return new ModelAndView("redirect:/login");
        }

        List<Appointment> appointments = appointmentService.getAppointmentsByPatientId(user.getId());
        ModelAndView mav = new ModelAndView("patient_appointments");
        mav.addObject("appointments", appointments);
        return mav;
    }

    // 📌 Show Doctor's Appointments
    @GetMapping("/doctor/appointments")
    public ModelAndView doctorAppointments(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return new ModelAndView("redirect:/login");
        }

        User user = (User) session.getAttribute("loggedInUser");
        if (user.getRole() != Role.DOCTOR) {
            return new ModelAndView("redirect:/login");
        }

        List<Appointment> appointments = appointmentService.getAppointmentsByDoctorId(user.getId());
        ModelAndView mav = new ModelAndView("doctor_appointments");
        mav.addObject("appointments", appointments);
        return mav;
    }
}
