package com.anudip.HealthcareSystem.controller;

import com.anudip.HealthcareSystem.model.User;
import com.anudip.HealthcareSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Show Registration Page (LoginPage is handled by Login Controller)
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    //Registration Form Submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        boolean isRegistered = userService.registerUser(user);

        if (isRegistered) {
            return "redirect:/login"; // Redirect to login page after successful registration
        } else {
            model.addAttribute("error", "Email already exists!");
            return "register";
        }
    }

}
