package com.example.backend.controller;

import com.example.backend.Exception.ResourceNotFoundException;
import com.example.backend.payload.JwtRequest;
import com.example.backend.payload.JwtResponse;
import com.example.backend.payload.UserDto;
import com.example.backend.security.JwtHelper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class AuthController {

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private JwtHelper helper;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){

        this.authenticateUser(request.getUsername(), request.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.helper.generateToken(userDetails);
        JwtResponse response = new JwtResponse();
        response.setToken(token);
        response.setUser(this.mapper.map(userDetails
                , UserDto.class));

        return new ResponseEntity<JwtResponse>(response, HttpStatus.ACCEPTED);
    }


// use to check username and password entered is authenticated (or correct ) or not
    private void authenticateUser(String username, String password){

        try{
            this.manager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            System.out.println("username: " + username + "password: " + password);

        }catch (BadCredentialsException e){
            throw new ResourceNotFoundException("Invalid Username or password");
        }catch (DisabledException e){
            throw new ResourceNotFoundException("User is not active");

        }
    }

}
