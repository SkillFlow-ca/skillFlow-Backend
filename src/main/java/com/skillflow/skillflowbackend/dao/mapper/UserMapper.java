package com.skillflow.skillflowbackend.dao.mapper;

import com.skillflow.skillflowbackend.dto.UserDTO;
import com.skillflow.skillflowbackend.dto.UserRegisterDTO;
import com.skillflow.skillflowbackend.dto.UserResponse;
import com.skillflow.skillflowbackend.model.User;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class UserMapper {

    @Autowired
    private ModelMapper modelMapper;

    // convert User Jpa Entity into UserDTO
    public UserDTO mapToUserDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    // Convert UserDTO to User JPA Entity
    public User mapToUser(UserRegisterDTO userRegisterDTO) {
        return modelMapper.map(userRegisterDTO, User.class);
    }


    // Convert UserDTO to User JPA Entity
    public UserResponse mapToUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }
}
