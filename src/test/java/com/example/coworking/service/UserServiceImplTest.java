package com.example.coworking.service;


import com.example.coworking.exceptions.CustomException;
import com.example.coworking.model.dto.request.UserRequestDto;
import com.example.coworking.model.dto.response.UserResponseDto;
import com.example.coworking.model.entity.User;
import com.example.coworking.model.enums.UserStatus;
import com.example.coworking.model.repositories.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepo userRepo;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createUser() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail("test@test.com");
        User user = new User();
        user.setId(1L);
        when(userRepo.save(any(User.class))).thenReturn(user);

        UserResponseDto result  = userService.createUser(userRequestDto);
        assertEquals(Optional.of(1L), Optional.of(result.getId()));
    }
    @Test(expected = CustomException.class)
    public void createUser_badEmail() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail("test@.test.com");
        userService.createUser(userRequestDto);
    }
    @Test(expected = CustomException.class)
    public void createUser_userExists() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail("test@test.com");
        User user = new User();
        user.setId(1L);
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));
        userService.createUser(userRequestDto);
    }

    @Test
    public void getUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        UserResponseDto result = userService.getUser(1L);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    public void getUserEntity() {
        User user = new User();
        user.setId(1L);
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        User result = userService.getUserEntity(1L);
        assertEquals(Optional.of(1L), Optional.of(result.getId()));
    }
    @Test(expected = CustomException.class)
    public void getUserEntity_notFound() {
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());
        userService.getUserEntity(1L);
    }

    @Test
    public void deleteUser() {
        User user = new User();
        user.setId(1L);
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);
        verify(userRepo, times(1)).save(any(User.class));
        assertEquals(UserStatus.DELETED, user.getStatus());
    }
    @Test
    public void getAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@test.com");
        user1.setStatus(UserStatus.CREATED);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@test.com");
        user2.setStatus(UserStatus.DELETED);

        when(userRepo.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserResponseDto> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(user1.getEmail(), users.get(0).getEmail());
    }
}