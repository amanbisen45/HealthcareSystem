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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DoctorController {

    @Autowired
    AppointmentService appointmentService;

    //Show Doctor Dashboard if He is logged In
    @GetMapping("/doctor/home")
    public ModelAndView doctorHome(HttpServletRequest request) {
        // Get existing session (don't create new one)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            // Redirect if not logged in
            return new ModelAndView("redirect:/login");
        }

        User user = (User) session.getAttribute("loggedInUser");
        if (user.getRole() != Role.DOCTOR) {
            // Restrict non-doctors
            return new ModelAndView("redirect:/login");
        }

        ModelAndView mav = new ModelAndView("doctor_home");
        // Send user details to view
        mav.addObject("user", user);
        return mav;
    }

    //Doctor Can View,Approve or Reject the Appointments
    @PostMapping("/doctor/update-appointment")
    public String updateAppointmentStatus(@RequestParam("appointmentId") Long appointmentId,
                                          @RequestParam("status") String status) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        if (appointment == null) {
            return "redirect:/doctor/home?error=AppointmentNotFound";
        }

        if (status.equalsIgnoreCase("ACCEPTED")) {
            appointment.setStatus(Status.APPROVED);
        } else if (status.equalsIgnoreCase("REJECTED")) {
            appointment.setStatus(Status.REJECTED);
        } else {
            return "redirect:/doctor/home?error=InvalidStatus";
        }

        appointmentService.saveAppointment(appointment);

        return "redirect:/doctor/home?success=AppointmentUpdated";
    }

}
