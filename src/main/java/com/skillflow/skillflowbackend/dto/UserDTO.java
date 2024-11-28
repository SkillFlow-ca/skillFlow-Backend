package com.skillflow.skillflowbackend.dto;

import com.skillflow.skillflowbackend.model.enume.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDTO {
    private long idUser;
    @NotBlank(message = "Invalid firstName: Empty firstName")
    @NotNull(message = "Invalid firstName: firstName is NULL")
    @Email(message = "Invalid email")
    private String firstName;
    @NotBlank(message = "Invalid lastName: Empty lastName")
    @NotNull(message = "Invalid lastName: lastName is NULL")
    @Email(message = "Invalid email")
    private String lastName;
    @NotBlank(message = "Invalid Email: Empty Email")
    @NotNull(message = "Invalid Email: Email is NULL")
    @Email(message = "Invalid email")
    private String email;
    private String phone;
    private Instant createdAt;
    private String profilePicture;
    private Boolean isValidated;
    @NotNull(message = "Invalid role type: roleType is NULL")
    private RoleType roleTypes;

}
