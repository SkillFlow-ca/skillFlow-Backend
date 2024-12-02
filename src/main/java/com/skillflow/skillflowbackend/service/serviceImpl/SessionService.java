package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    UserRepository userRepository;

    public Optional<User> getUserBySession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null ) {
            String mail = (String) authentication.getPrincipal();
            return userRepository.findByEmail(mail);
        } else {
            return null; // or throw an exception, depending on your requirements
        }
    }

}
