package com.studentsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentsystem.dto.request.UserCreate;
import com.studentsystem.dto.request.UserLoginRequest;
import com.studentsystem.dto.response.SuccessLogin;
import com.studentsystem.dto.response.SuccessUserCreated;
import com.studentsystem.service.interfaces.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/create")
    public ResponseEntity<SuccessUserCreated> createUser(@RequestBody UserCreate userCreate) {
        return ResponseEntity.ok(userService.createUser(userCreate));
    }

    @PostMapping("/passport/token")
    public ResponseEntity<SuccessLogin> getAccessToken(@RequestBody UserLoginRequest userLoginRequest) {
        return ResponseEntity.ok(userService.getAccessToken(userLoginRequest));
    }
}
