package com.anudip.HealthcareSystem.controller;

import com.anudip.HealthcareSystem.model.Role;
import com.anudip.HealthcareSystem.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PatientController {

    @GetMapping("/patient/home")
    public ModelAndView patientHome(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Get existing session (don't create new one)
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return new ModelAndView("redirect:/login"); // Redirect if not logged in
        }

        User user = (User) session.getAttribute("loggedInUser");
        if (user.getRole() != Role.PATIENT) {
            return new ModelAndView("redirect:/login"); // Restrict non-patients
        }

        ModelAndView mav = new ModelAndView("patient_home");
        mav.addObject("user", user); // Send user details to view
        return mav;
    }
}
