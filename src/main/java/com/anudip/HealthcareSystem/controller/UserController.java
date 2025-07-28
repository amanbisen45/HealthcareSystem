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

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                               @RequestParam(required = false) String speciality,
                               @RequestParam(required = false) Integer experience,
                               Model model) {
        boolean isRegistered = userService.registerUser(user, speciality, experience);

        if (isRegistered) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Email already exists!");
            return "register";
        }
    }

}
