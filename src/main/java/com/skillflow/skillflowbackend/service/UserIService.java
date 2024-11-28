package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.dto.*;
import jakarta.mail.MessagingException;

public interface UserIService {
    public UserDTO createUserAccount(UserRegisterDTO userRegisterDTO) throws MessagingException;
    public UserDTO createUserAccountForAll(UserRegisterDTO userRegisterDTO) throws MessagingException;

    public UserDTO createUserByAdmin(UserRegisterDTO userRegisterDTO);
    public UserDTO createAdminAccount(UserRegisterDTO userRegisterDTO) throws MessagingException;

    public ResponseDto validateAccount(Long verificationCode) throws MessagingException;

    public JwtResponse login(String email);

    public ResponseDto forgotPassword(String email) throws MessagingException;
    public ResponseDto resetPassword(Long token, ResetPasswordRequest newPassword);}
