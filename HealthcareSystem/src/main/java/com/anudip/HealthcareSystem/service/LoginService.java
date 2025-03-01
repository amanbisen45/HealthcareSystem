package com.anudip.HealthcareSystem.service;

import com.anudip.HealthcareSystem.model.User;
import com.anudip.HealthcareSystem.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private LoginRepository repo;

    public User login(String username, String password) {
        User user = repo.findByEmailAndPassword(username, password);
        return user;
    }

}