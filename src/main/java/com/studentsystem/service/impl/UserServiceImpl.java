package com.studentsystem.service.impl;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.studentsystem.models.user.Student;
import com.studentsystem.models.user.Teacher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.studentsystem.dto.request.UserCreate;
import com.studentsystem.dto.request.UserLoginRequest;
import com.studentsystem.dto.response.SuccessLogin;
import com.studentsystem.dto.response.SuccessUserCreated;
import com.studentsystem.exception.custom.EmailAlreadyInUseException;
import com.studentsystem.models.User;
import com.studentsystem.models.user.Chancellor;
import com.studentsystem.repository.UserRepository;
import com.studentsystem.service.interfaces.UserService;
import com.studentsystem.utils.JwtUtils;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtUtils jwtUtils
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public SuccessUserCreated createUser(UserCreate userCreateRequest) {
        String email = userCreateRequest.getEmail();
        Optional<User> doesExist = userRepository.findByEmail(email);
        if (doesExist.isPresent()) {
            throw new EmailAlreadyInUseException("Username already exists");
        }
        if ("ADMIN".equals(userCreateRequest.getRole())) {
            throw new IllegalArgumentException("You can't create an admin user");
        }
        if (!List.of("STUDENT","CHANCELLOR","TEACHER").contains(userCreateRequest.getRole())) {
            throw new InvalidParameterException("Role not valid");
        }
        if ("CHANCELLOR".equals(userCreateRequest.getRole())) {
            Chancellor chancellor = new Chancellor();
            chancellor.setEmail(userCreateRequest.getEmail());
            chancellor.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
            chancellor.setRole(userCreateRequest.getRole());
            chancellor.setFullName(userCreateRequest.getFullName());
            chancellor.setCreated_at(LocalDateTime.now());
            userRepository.save(chancellor);
        }
        else if ("TEACHER".equals(userCreateRequest.getRole())) {
            if (userCreateRequest.getDepartment() == null || userCreateRequest.getSpecialty() == null) {
                throw new InvalidParameterException("Missing specialty or department");
            }
            Teacher teacher = new Teacher();
            teacher.setEmail(userCreateRequest.getEmail());
            teacher.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
            teacher.setRole(userCreateRequest.getRole());
            teacher.setFullName(userCreateRequest.getFullName());
            teacher.setCreated_at(LocalDateTime.now());

            teacher.setDepartment(userCreateRequest.getDepartment());
            teacher.setSpecialty(userCreateRequest.getSpecialty());
            userRepository.save(teacher);
        }
        else if ("STUDENT".equals(userCreateRequest.getRole())) {
            if (userCreateRequest.getLevel().isEmpty() || userCreateRequest.getDepartment().isEmpty()) {
                throw new InvalidParameterException("Missing specialty or department");
            }
            Student student = new Student();
            student.setEmail(userCreateRequest.getEmail());
            student.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
            student.setRole(userCreateRequest.getRole());
            student.setFullName(userCreateRequest.getFullName());
            student.setCreated_at(LocalDateTime.now());

            student.setDepartment(userCreateRequest.getDepartment());
            student.setLevel(userCreateRequest.getLevel());
            userRepository.save(student);
        }

        return new SuccessUserCreated(
                userCreateRequest.getEmail(),
                userCreateRequest.getFullName(),
                userCreateRequest.getRole()
        );
    }
    
    public SuccessLogin getAccessToken(UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null || userLoginRequest.getEmail() == null || userLoginRequest.getPassword() == null) {
            throw new IllegalArgumentException("Email and password are required");
        }

        Optional<User> user = userRepository.findByEmail(userLoginRequest.getEmail());
        if (user.isEmpty()) {
            throw new RuntimeException("Username or Password incorrect");
        }

        User existingUser = user.get();
        if (!passwordEncoder.matches(userLoginRequest.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Username or Password incorrect");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", existingUser.getEmail());
        claims.put("role", existingUser.getRole());
        claims.put("fullName", existingUser.getFullName());
        claims.put("userId", existingUser.getUserId());

        String accessToken = jwtUtils.generateAccessToken(existingUser.getEmail(), claims);
        return new SuccessLogin(accessToken, 900);
    }
}
