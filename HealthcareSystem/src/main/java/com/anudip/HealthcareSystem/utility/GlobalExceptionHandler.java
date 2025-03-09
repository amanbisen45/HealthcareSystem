package com.anudip.HealthcareSystem.utility;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleDuplicateEntry(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "Appointment slot already booked! Please choose a different time.");
        return modelAndView;
    }
}