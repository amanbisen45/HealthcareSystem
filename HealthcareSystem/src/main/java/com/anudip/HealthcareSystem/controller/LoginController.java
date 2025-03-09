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
        // This looks for index.html inside src/main/resources/templates/
        return "index";
    }


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
            // Store user in session
            session.setAttribute("loggedInUser", oauthUser);

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
        // Destroy session on logout
        request.getSession().invalidate();
        return "redirect:/login";
    }

    @GetMapping("/user/status")
    @ResponseBody
    public Map<String, Object> getUserStatus(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Map<String, Object> response = new HashMap<>();

        if (session != null) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");

            if (loggedInUser != null) {
                response.put("loggedIn", true);
                response.put("role", loggedInUser.getRole().toString());
                return response;
            }
        }
        response.put("loggedIn", false);
        return response;
    }

}
