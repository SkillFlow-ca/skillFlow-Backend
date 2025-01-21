package com.skillflow.skillflowbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AuthConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    public AuthConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    @Bean
    public AuthenticationManager autenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/v1/auth/user/register",
                                "/api/v1/auth/admin/register",
                                "/api/v1/auth/logout",
                                "/api/v1/auth/getUsers",
                                "/api/v1/auth/resetPassword/**",
                                "/api/v1/auth/token",
                                "/api/v1/auth/forgotPassword",
                                "/api/v1/auth/register",
                                "/api/v1/auth/validateAccount/**",
                                "/api/v1/blog/getAllBlogBySatusAndOrderedByCreatedAt",
                                "/api/v1/blog/get",
                                "/api/v1/job/getJobsAdminByContraints",
                                "/api/v1/candidate/addCandidateToJob",
                                "/api/v1/candidate/updateResume",
                                "/api/v1/candidate/delete",
                                "/api/v1/candidate/candidates",
                                "/api/v1/candidate/getByTrackCode",
                                "/api/v1/question/getQuestionById",
                                "/api/v1/question/countQuesions",
                                "/api/v1/question/getQuestionByContraintes",
                                "/api/v1/answer/getAnswersByQuestionId",
                                "/api/v1/course/getAllCourses",
                                "/api/v1/course/GetCourse",
                                "/api/v1/blog/getBlogToLandingPage",
                                "/api/v1/course/getCoursesForLandingPage",
                                "/api/v1/course/getPublishedCourses",
                                "/api/v1/contact/save",
                                "/api/v1/auth/findUserByCourseId",
                                "/api/v1/courseCategory/getAllNotDeleted",
                                "/user-service/v3/api-docs/**", "/actuator/health")
                        .permitAll().anyRequest()
                        .authenticated())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // To swagger authorization
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200","http://myawsskillfront.s3-website.ca-central-1.amazonaws.com")); // Specify the frontend origin
            configuration.setAllowedMethods(Arrays.asList("*"));
            configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
            configuration.setAllowCredentials(true);

            return configuration;
        }));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

}
