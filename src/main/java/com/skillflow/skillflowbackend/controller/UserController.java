package com.skillflow.skillflowbackend.controller;

import com.skillflow.skillflowbackend.dto.*;
import com.skillflow.skillflowbackend.exception.UserServiceCustomException;
import com.skillflow.skillflowbackend.model.User;
import com.skillflow.skillflowbackend.model.enume.RoleType;
import com.skillflow.skillflowbackend.repository.UserRepository;
import com.skillflow.skillflowbackend.service.UserIService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/auth/")
@Validated
public class UserController {

    @Autowired
    UserIService userIService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GOOGLE_TOKEN_VALIDATION_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";

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


    @PostMapping("/loginGoogle")
    public JwtResponse googleLogin(@RequestBody Map<String, String> request) {
        String idToken = request.get("token"); // Récupérer l'ID Token envoyé depuis Angular
        System.out.println(idToken);
        String googleTokenValidationUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(googleTokenValidationUrl, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> userData = response.getBody();

            // Exemple : Extraire les infos de l'utilisateur
            String email = (String) userData.get("email");
            String name = (String) userData.get("name");
            String picture = (String) userData.get("picture");
            Optional<User> optionalUser = userRepository.findByEmail(email);
            User user;
            if (optionalUser.isEmpty()) {
                user = new User();
                user.setEmail(email);
                user.setFirstName(name);
                user.setIsValidated(true);
                user.setCreatedAt(Instant.now());
                user.setIsDeleted(false);
                user.setRoleTypes(RoleType.USER);
                userRepository.save(user);
                return userIService.login(email);
            } else {
                user = optionalUser.get();
            }

            // Step 4: Return the JWT response
            return userIService.login(email);
        }
       return null;
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
    @GetMapping("/getUsers")
    public ResponseEntity<ResponseModel<UserDTO>> getUsers(
            @RequestParam(required = false,defaultValue="1")int pageNo,
            @RequestParam(required = false,defaultValue="10")int size) {
        Pageable pageRequestData = PageRequest.of(pageNo - 1, size);
        ResponseModel<UserDTO> usersActive=userIService.getUsers(pageRequestData);
        return new ResponseEntity<>(usersActive, HttpStatus.PARTIAL_CONTENT);
    }
    @GetMapping("/userinsesson")
    public User getUserBySession(){
        return userIService.getUserBySession();
    }
    @PutMapping("/updateAccount")
    public User updateUserToAddImage(MultipartFile img) throws IOException {
            return userIService.updateUserToAddImage(img);
    }

    @PostMapping("/addUserByAdmin")
    public UserDTO createUserByAdmin(@RequestBody UserRegisterDTO userRegisterDTO) {
    return userIService.createUserByAdmin(userRegisterDTO);
    }
    @PutMapping("/deleteUser")
    public void deleteUser(@RequestParam Long id){
        userIService.deleteUser(id);
    }

    @GetMapping("/getStatisticsUsers")
    public ResponseEntity<Map<String, Long>> getStatisticsUsers() {
        return userIService.getStatisticsUsers();
    }

    @GetMapping("/findUserByCourseId")
    public User findUserByCourseId(@RequestParam Long courseId) {
        return userIService.findUserByCourseId(courseId);
    }
    }
