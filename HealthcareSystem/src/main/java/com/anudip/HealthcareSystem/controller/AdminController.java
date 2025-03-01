package com.anudip.HealthcareSystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {
    @GetMapping("/admin/dashboard")
    public ModelAndView patientHome() {
        ModelAndView mav = new ModelAndView("admin_dashboard");
        return mav;
    }
}
