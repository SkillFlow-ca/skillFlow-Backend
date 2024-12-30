package com.skillflow.skillflowbackend.service.serviceImpl;

import com.skillflow.skillflowbackend.config.AuthenticationConstants;
import com.skillflow.skillflowbackend.dao.mapper.UserMapper;
import com.skillflow.skillflowbackend.dto.*;
import com.skillflow.skillflowbackend.exception.UserServiceCustomException;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.model.enume.RoleType;
import com.skillflow.skillflowbackend.repository.UserRepository;
import com.skillflow.skillflowbackend.service.UserIService;
import com.skillflow.skillflowbackend.utility.EmailUtility;
import com.skillflow.skillflowbackend.utility.ResponseUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserIService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JWTService jwtService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    private EmailUtility emailUtility;

    @Autowired
    private ResponseUtil responseUtil;

    @Autowired
    private SessionService sessionService;

    private Random random = new Random();
    @Override
    public UserDTO createUserAccount(UserRegisterDTO userRegisterDTO) throws MessagingException {
        if(userRegisterDTO.getRoleTypes()!= RoleType.USER){
            throw new UserServiceCustomException("Unsupported role type", "UNSUPPORTED_ROLE_TYPE",
                    HttpStatus.BAD_REQUEST);
        }
        userRegisterDTO.setRoleTypes(RoleType.USER);
        return createAccount(userRegisterDTO,RoleType.USER);
    }

    @Override
    public User updateUserToAddImage(MultipartFile img) throws IOException {
        Optional<User> userOptional = userRepository.findUserByEmail(sessionService.getUserBySession().orElseThrow(
                () -> new IllegalStateException("No user found in the current session.")
        ).getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setProfilePicture(img.getBytes());
            return userRepository.save(user); // Save and return updated user
        } else {
            throw new NoSuchElementException("User not found with the given email.");
        }
    }

    @Override
    public UserDTO createUserAccountForAll(UserRegisterDTO userRegisterDTO) throws MessagingException {
        return createAccount(userRegisterDTO,userRegisterDTO.getRoleTypes());
    }

    @Override
    public UserDTO createUserByAdmin(UserRegisterDTO userRegisterDTO) {
        User user=userMapper.mapToUser(userRegisterDTO);
        user.setCreatedAt(Instant.now());
        user.setIsValidated(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleTypes(userRegisterDTO.getRoleTypes());
        userRepository.save(user);
        return userMapper.mapToUserDto(user);
    }

    @Override
    public UserDTO createAdminAccount(UserRegisterDTO userRegisterDTO) throws MessagingException {
        if(userRegisterDTO.getRoleTypes()!= RoleType.ADMIN){
            throw new UserServiceCustomException("Unsupported role type", "UNSUPPORTED_ROLE_TYPE",
                    HttpStatus.BAD_REQUEST);
        }
        userRegisterDTO.setRoleTypes(RoleType.ADMIN);
        return createAccount(userRegisterDTO,RoleType.ADMIN);
    }

    @Override
    public ResponseDto validateAccount(Long verificationCode) throws MessagingException {
        User user=userRepository.findByTokenToValidate(verificationCode);
        if (Boolean.FALSE.equals(user.getTokenToValidate()) && !user.getTokenToValidate().equals(verificationCode)) {
            throw new UserServiceCustomException("Invalid verification code!", "INVALID_CODE");
        }
        if (isTokenExpired(user.getValidateCodeCreationDate())) {
            user.setTokenToValidate(generateOTPToSend());
            user.setValidateCodeCreationDate(LocalDateTime.now());
            user = userRepository.save(user);
            emailUtility.sendVerificationEmail(user.getEmail(), user.getFirstName(), user.getTokenToValidate(),
                    user.getRoleTypes());
            return responseUtil.createResponse("Current token experied!! Sent a new Token", "SUCCESS",
                    "Check your email :" + user.getEmail()
                            + " and OTP token to reset your password. If you didn't receive contact support team.");
        }
        if (Boolean.TRUE.equals(user.getTokenToValidate())) {
            return responseUtil.createResponse("Your account is already validated", "SUCCESS",
                    "Account already validated");
        }
        user.setIsValidated(true);
        user.setTokenToValidate(null);
        userRepository.save(user);
        return responseUtil.createResponse("Your account is validated", "SUCCESS", "Account validated successfully");
    }

    @Override
    public JwtResponse login(String email) {
        String token = null;
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserServiceCustomException("No user found with this email:" + email, "User_Not_Found"));
        if (!user.getIsValidated()) {
            throw new UserServiceCustomException("You need to validate your account first and check your email",
                    "ACCOUNT_NOT_VALID", HttpStatus.FORBIDDEN);
        } else {
            token = jwtService.generateToken(email);
            UserResponse userResponse = userMapper.mapToUserResponse(user);
            userRepository.save(user);
            return new JwtResponse(userResponse, token);
        }
    }

    @Override
    public ResponseDto forgotPassword(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserServiceCustomException("No user found with this email: " + email,
                        "USER_NOT_FOUND", HttpStatus.NOT_FOUND));
        if (Boolean.FALSE.equals(user.getIsValidated())) {
            throw new UserServiceCustomException("You need to validate your account first.",
                    "ACCOUNT_NOT_VALID", HttpStatus.FORBIDDEN);
        }
        if (user.getTokenToForgotPassword() != null && !isTokenExpired(user.getTokenToForgotPasswordCreationDate())) {
            return responseUtil.createResponse("Your forgot password OTP was already sent", "OTP_ALREADY_SENT",
                    "Check your email :" + email + "and OTP token to reset your password");
        }

        user.setTokenToForgotPassword(generateOTPToSend());
        user.setTokenToForgotPasswordCreationDate(LocalDateTime.now());
        user = userRepository.save(user);
        emailUtility.sendForgetPasswordEmail(user.getEmail(), user.getFirstName(), user.getTokenToForgotPassword());
        return responseUtil.createResponse("Your Token is successfully sended", "SUCCESS",
                "Check your email :" + email + "and OTP token to reset your password");
    }

    @Override
    public ResponseDto resetPassword(Long token, ResetPasswordRequest newPassword) {
        User user=userRepository.findByTokenToForgotPassword(token);
        if (!Objects.equals(user.getTokenToForgotPassword(), token)) {
            throw new UserServiceCustomException("Token Invalid", "Token invalid");
        }
        LocalDateTime tokenCreationDate = user.getTokenToForgotPasswordCreationDate();
        if (isTokenExpired(tokenCreationDate)) {
            return responseUtil.createResponse("Token expired", "Failed", "Token expired");
        }
        System.out.println(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword.getPassword()));
        user.setTokenToForgotPassword(null);
        user.setTokenToForgotPasswordCreationDate(null);
        userRepository.save(user);
        return responseUtil.createResponse("Password Updated", "SUCCESS", "Your password successfully updated.");
    }

    @Override
    public ResponseModel<UserDTO>  getUsers(Pageable pageable) {
        Page<User> users=userRepository.findUsers(pageable);
        return buildResponse(users);
    }

    @Override
    public User getUserBySession() {
        User user=sessionService.getUserBySession().get();
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        User user=userRepository.findById(id).orElseThrow(()->new UserServiceCustomException("No user found with this id: "+id,"USER_NOT_FOUND"));
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {
        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);
        return diff.toMinutes() >= AuthenticationConstants.EXPIRE_TOKEN_AFTER_MINUTES;
    }

    public UserDTO createAccount(UserRegisterDTO userRegisterDTO, RoleType roleType) throws MessagingException {
        if(userRepository.existsByEmail(userRegisterDTO.getEmail())){
            throw new UserServiceCustomException("Email must be unique","DUPLICATED_EMAIL");
        }
        User user=userMapper.mapToUser(userRegisterDTO);
        user.setCreatedAt(Instant.now());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setIsValidated(false);
        user.setRoleTypes(roleType);
        user.setTokenToValidate(generateOTPToSend());
        user.setValidateCodeCreationDate(LocalDateTime.now());
        User userSave=userRepository.save(user);
        UserDTO userDTO=userMapper.mapToUserDto(userSave);
        emailUtility.sendVerificationEmail(userRegisterDTO.getEmail(), userRegisterDTO.getFirstName(),
                user.getTokenToValidate(), user.getRoleTypes());
        return userDTO;
    }
    private Long generateOTPToSend() {
        int min = 10000;
        int max = 99999;
        return Long.valueOf(this.random.nextInt(max - min + 1) + min);
    }


    private ResponseModel<UserDTO> buildResponse(Page<User> userPage) {
        List<UserDTO> listOfUser = userPage.toList()
                .stream()
                .map(userMapper::mapToUserDto)
                .collect(Collectors.toList());

        return ResponseModel.<UserDTO>builder()
                .pageNo(userPage.getNumber())
                .pageSize(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .data(listOfUser)
                .isLastPage(userPage.isLast())
                .build();
    }
}
