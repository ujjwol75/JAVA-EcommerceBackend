package com.example.backend.payload;

public class JwtResponse {

    private String token;
    private UserDto user;

    public String getToken() {
        return token;
    }

    public JwtResponse() {
    }
    

    public void setToken(String token) {
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "JwtResponse{" +
                "token='" + token + '\'' +
                ", user=" + user +
                '}';
    }
}
