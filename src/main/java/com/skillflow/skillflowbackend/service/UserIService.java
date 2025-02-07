package com.skillflow.skillflowbackend.service;

import com.skillflow.skillflowbackend.dto.*;
import com.skillflow.skillflowbackend.model.User;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface UserIService {
    public UserDTO createUserAccount(UserRegisterDTO userRegisterDTO) throws MessagingException;
    public User updateUserToAddImage(MultipartFile img) throws IOException;

    public UserDTO createUserAccountForAll(UserRegisterDTO userRegisterDTO) throws MessagingException;

    public UserDTO createUserByAdmin(UserRegisterDTO userRegisterDTO);
    public UserDTO createAdminAccount(UserRegisterDTO userRegisterDTO) throws MessagingException;

    public ResponseDto validateAccount(Long verificationCode) throws MessagingException;

    public JwtResponse login(String email);

    public ResponseDto forgotPassword(String email) throws MessagingException;
    public ResponseDto resetPassword(Long token, ResetPasswordRequest newPassword);

    public ResponseModel<UserDTO>  getUsers(Pageable pageable);

    public User getUserBySession();
    public void deleteUser(Long id);

    public ResponseEntity<Map<String, Long>> getStatisticsUsers();

    public User findUserByCourseId(Long courseId);



}
