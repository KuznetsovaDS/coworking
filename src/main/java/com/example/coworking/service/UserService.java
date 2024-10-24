package com.example.coworking.service;

import com.example.coworking.model.dto.request.UserRequestDto;
import com.example.coworking.model.dto.response.UserResponseDto;
import com.example.coworking.model.entity.User;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequest);

    UserResponseDto getUser(Long id);

    UserResponseDto update(Long id, UserRequestDto request);

    void deleteUser(Long id);

    List<UserResponseDto> getAllUsers();

    User getUserEntity(Long id);


}

