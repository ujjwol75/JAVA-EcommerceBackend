package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.payload.UserDto;
import com.example.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Create User
    public UserDto createUser(UserDto userDto){
        User user = this.mapper.map(userDto, User.class);

        String password = user.getPassword();
        String encode = this.passwordEncoder.encode(password);
        user.setPassword(encode);

        User save = this.userRepository.save(user);
        UserDto uDto = this.mapper.map(save, UserDto.class);
        return uDto;
    }

    //$2a$10$UVCvWg9huBzOP.Axw8ZGOemxfXteY8WfILU2Lmvu06aQ1EnrxO2ui

    //get user by userId
    public UserDto getUserById(int userId){
        User user = this.userRepository.findByuserId(userId);
        UserDto map = this.mapper.map(user, UserDto.class);

        return map;
    }

    //get All Users
    public List<UserDto> getAllUsers(){
        List<User> allUser = this.userRepository.findAll();
        List<UserDto> collect = allUser.stream().map(each -> this.mapper.map(each, UserDto.class)).collect(Collectors.toList());

        return collect;
    }

    //delete user by UserId
    public void deleteUser(int userId){
        this.userRepository.deleteById(userId);
    }


}
