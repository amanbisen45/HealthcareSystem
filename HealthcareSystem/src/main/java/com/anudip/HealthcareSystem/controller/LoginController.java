package com.anudip.HealthcareSystem.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.anudip.HealthcareSystem.model.User;
import com.anudip.HealthcareSystem.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    private LoginService userService;

    @GetMapping("/")
    public String home() {
        return "index"; // This looks for index.html inside src/main/resources/templates/
    }

//    @GetMapping("/user/status")
//    public Map<String, Object> getUserStatus(@SessionAttribute(name = "userRole", required = false) String userRole) {
//        Map<String, Object> response = new HashMap<>();
//        if (userRole == null) {
//            response.put("loggedIn", false);
//        } else {
//            response.put("loggedIn", true);
//            response.put("role", userRole);
//        }
//        return response;
//    }



    //Login Page using ModelAndView
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("user", new User());
        return mav;
    }

    //Login after validating email & password based on Role (Patient/Doctor)
    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, HttpServletRequest request) {
        User oauthUser = userService.login(user.getEmail(), user.getPassword());

        if (Objects.nonNull(oauthUser)) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", oauthUser);  // Store user in session

            switch (oauthUser.getRole()) {
                case PATIENT:
                    return "redirect:/patient/home";
                case DOCTOR:
                    return "redirect:/doctor/home";
                case ADMIN:
                    return "redirect:/admin/dashboard";
                default:
                    return "redirect:/login";
            }
        } else {
            return "redirect:/login?error=true";
        }
    }

    @RequestMapping(value = {"/logout"}, method = RequestMethod.POST)
    public String logoutDo(HttpServletRequest request) {
        request.getSession().invalidate();  // Destroy session on logout
        return "redirect:/login";
    }
}
