package com.example.coworking.service;

import com.example.coworking.exceptions.CustomException;
import com.example.coworking.model.dto.request.UserRequestDto;
import com.example.coworking.model.dto.response.UserResponseDto;
import com.example.coworking.model.entity.User;
import com.example.coworking.model.enums.UserStatus;
import com.example.coworking.model.repositories.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final ObjectMapper mapper;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        String email = userRequestDto.getEmail();

        if(!EmailValidator.getInstance().isValid(email)){
            throw new CustomException("Invalid email", HttpStatus.BAD_REQUEST);
        }

        userRepo.findByEmail(email)
                .ifPresent(u -> {
                    throw  new CustomException("User already exist", HttpStatus.CONFLICT);
                });
        User user = mapper.convertValue(userRequestDto, User.class);
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(UserStatus.CREATED);
        User save = userRepo.save(user);
        return mapper.convertValue(save, UserResponseDto.class);
    }
    @Override
    public UserResponseDto getUser(Long id) {
        User user = getUserEntity(id);
        return mapper.convertValue(user, UserResponseDto.class);
    }
    @Override
    public User getUserEntity(Long id) {
        return userRepo.findById(id)
                .orElseThrow(()-> new CustomException("User is not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public UserResponseDto update(Long id, UserRequestDto request) {
        User user = getUserEntity(id);

        user.setEmail(StringUtils.isBlank(request.getEmail()) ? user.getEmail() : request.getEmail());
        user.setFirstName(StringUtils.isBlank(request.getFirstName()) ? user.getFirstName() : request.getFirstName());
        user.setLastName(StringUtils.isBlank(request.getLastName()) ? user.getLastName() : request.getLastName());
        user.setPassword(StringUtils.isBlank(request.getPassword()) ? user.getPassword() : request.getPassword());

        user.setStatus(UserStatus.UPDATED);
        user.setUpdatedAt(LocalDateTime.now());
        User save = userRepo.save(user);
        return mapper.convertValue(save, UserResponseDto.class);
    }

    @Override
    public void deleteUser(Long id) {
       User user = getUserEntity(id);
       user.setStatus(UserStatus.DELETED);
       user.setUpdatedAt(LocalDateTime.now());
       userRepo.save(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepo.findAll().stream()
                .filter(u -> u.getStatus() != UserStatus.DELETED)
                .map(u->mapper.convertValue(u, UserResponseDto.class))
                .collect(Collectors.toList());
    }
}
