package com.example.backend.controller;

import com.example.backend.payload.ApiResponse;
import com.example.backend.payload.UserDto;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //Create User
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        Date date = new Date();
        userDto.setDate(date);
        UserDto user1 = this.userService.createUser(userDto);
        return new ResponseEntity<>(user1, HttpStatus.ACCEPTED);

    }

    //get user by userId
    @GetMapping("/findById/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") int userId){
        UserDto userById = this.userService.getUserById(userId);
        return new ResponseEntity<>(userById, HttpStatus.FOUND);

    }

    //get All users
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> allUsers = this.userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.FOUND);
    }

    //Delete User by UserId
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") int userId){
        this.userService.deleteUser(userId);
        return null;
    }

    //update



}
