package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.dto.*;
import com.skillflow.skillflowbackend.exception.UserServiceCustomException;
import com.skillflow.skillflowbackend.service.UserIService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@CrossOrigin("**")
@RestController
@RequestMapping("/api/v1/auth/")
@Validated
public class UserController {

    @Autowired
    UserIService userIService;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/user/register")
    public UserDTO createUserAccount(@Valid  @RequestBody UserRegisterDTO userRegisterDTO) throws MessagingException {
        return userIService.createUserAccount(userRegisterDTO);
    }
    @PostMapping("/admin/register")
    public UserDTO createAdminAccount(@Valid  @RequestBody UserRegisterDTO userRegisterDTO) throws MessagingException {
        return userIService.createAdminAccount(userRegisterDTO);
    }
    @PutMapping("/validateAccount/{verificationCode}")
    public ResponseDto validateAccount(@PathVariable Long verificationCode) throws MessagingException {
        return userIService.validateAccount(verificationCode);
    }
    @PostMapping("/token")
    public JwtResponse login(@Valid @RequestBody LoginRequestDTO loginData) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getPassword()));
            return userIService.login(loginData.getEmail());
        } catch (AuthenticationException e) {
            throw new UserServiceCustomException("Invalid Information", "BAD_LOGIN_CREDENTIALS");
        }
    }
    @PostMapping("/forgotPassword")
    public ResponseDto forgotPassword( @NotBlank(message = "Invalid Email: Empty Email")
                                       @NotNull(message = "Invalid Email: Email is NULL")
                                       @Email(message = "Invalid email")
                                       @RequestParam String email) throws MessagingException {
        return userIService.forgotPassword(email);
    }
    @PutMapping("/resetPassword/{token}")
    public ResponseDto resetPassword(@PathVariable Long token,@RequestBody ResetPasswordRequest  newPassword) {
        return userIService.resetPassword(token, newPassword);
    }
}
