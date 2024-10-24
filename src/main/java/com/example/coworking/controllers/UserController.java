package com.example.coworking.controllers;

import com.example.coworking.model.dto.request.UserRequestDto;
import com.example.coworking.model.dto.response.UserResponseDto;
import com.example.coworking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Users")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary =  "Create user")
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto){
        return userService.createUser(userRequestDto);
    }
    @GetMapping("/{id}")
    @Operation(summary =  "Get user")
       public UserResponseDto readUser(@PathVariable Long id){
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    @Operation(summary =  "Update user")
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto){
        return userService.update(id, userRequestDto);
    }
    @DeleteMapping("/{id}")
    @Operation(summary =  "Delete user")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
    @GetMapping("/all")
    @Operation(summary =  "Get all user")
    public List<UserResponseDto> getAllUsers(){
        return userService.getAllUsers();
    }
}
