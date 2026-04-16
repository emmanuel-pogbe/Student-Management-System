package com.studentsystem.service.impl;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.studentsystem.dto.request.UserCreate;
import com.studentsystem.dto.request.UserLoginRequest;
import com.studentsystem.dto.response.SuccessLogin;
import com.studentsystem.dto.response.SuccessUserCreated;
import com.studentsystem.enums.RoleEnum;
import com.studentsystem.exception.custom.EmailAlreadyInUseException;
import com.studentsystem.models.StudentProfile;
import com.studentsystem.models.User;
import com.studentsystem.repository.StudentProfileRepository;
import com.studentsystem.repository.UserRepository;
import com.studentsystem.service.interfaces.UserService;
import com.studentsystem.utils.JwtUtils;


@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Value("${studentapplication.password}")
    private String applicationPassword;

    public UserServiceImpl(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtUtils jwtUtils,
        StudentProfileRepository studentProfileRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.studentProfileRepository = studentProfileRepository;
    }

    public SuccessUserCreated createUser(UserCreate userCreateRequest) {
        String email = userCreateRequest.getEmail();
        Optional<User> doesExist = userRepository.findByEmail(email);
        RoleEnum userRole = userCreateRequest.getUserRole();
        if (userRole == null) {
            throw new RuntimeException("User role is not valid");
        }
        if (doesExist.isPresent()) {
            throw new EmailAlreadyInUseException("Email already exists");
        }
        if (!List.of("STUDENT","CHANCELLOR","TEACHER","ADMIN").contains(userRole.name())) {
            throw new InvalidParameterException("Role not valid");
        }
        if ("ADMIN".equals(userRole.name())) {
            if (userCreateRequest.getApplicationPassword() == null) {
                throw new IllegalArgumentException("You can't create an admin user");
            }
            if (!applicationPassword.equals(userCreateRequest.getApplicationPassword())) {
                throw new IllegalArgumentException("You can't create an admin user");
            }
            User admin = new User();
            admin.setEmail(userCreateRequest.getEmail());
            admin.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
            admin.setUserRole(userRole);
            admin.setFullName(userCreateRequest.getFullName());
            admin.setCreated_at(LocalDateTime.now());
            userRepository.save(admin);
        }

        else if ("CHANCELLOR".equals(userRole.name())) {
            User chancellor = new User();
            chancellor.setEmail(userCreateRequest.getEmail());
            chancellor.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
            chancellor.setUserRole(userCreateRequest.getUserRole());
            chancellor.setFullName(userCreateRequest.getFullName());
            chancellor.setCreated_at(LocalDateTime.now());
            userRepository.save(chancellor);
        }
        else if ("TEACHER".equals(userRole.name())) {
            if (userCreateRequest.getDepartment() == null || userCreateRequest.getSpecialty() == null) {
                throw new InvalidParameterException("Missing specialty or department");
            }
            User teacher = new User();
            teacher.setEmail(userCreateRequest.getEmail());
            teacher.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
            teacher.setUserRole(userRole);
            teacher.setFullName(userCreateRequest.getFullName());
            teacher.setCreated_at(LocalDateTime.now());

            // extra attributes for teacher in the future
            // teacher.setDepartment(userCreateRequest.getDepartment());
            // teacher.setSpecialty(userCreateRequest.getSpecialty());
            userRepository.save(teacher);
        }
        else if ("STUDENT".equals(userRole.name())) {
            if (userCreateRequest.getLevel().isEmpty() || userCreateRequest.getDepartment().isEmpty()) {
                throw new InvalidParameterException("Missing level or department");
            }
            User student = new User();
            student.setEmail(userCreateRequest.getEmail());
            student.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
            student.setUserRole(userRole);
            student.setFullName(userCreateRequest.getFullName());
            student.setCreated_at(LocalDateTime.now());
            
            // extra attributes for students in the future
            StudentProfile profileInfo = new StudentProfile();
            profileInfo.setDepartment(userCreateRequest.getDepartment());
            profileInfo.setLevel(userCreateRequest.getLevel());
            profileInfo.setUser(student);

            userRepository.save(student);

            // save extra student information
            studentProfileRepository.save(profileInfo);
        }

        return new SuccessUserCreated(
                userCreateRequest.getEmail(),
                userCreateRequest.getFullName(),
                userRole
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
        claims.put("role", existingUser.getUserRole());
        claims.put("fullName", existingUser.getFullName());
        claims.put("userId", existingUser.getUserId());

        String accessToken = jwtUtils.generateAccessToken(existingUser.getEmail(), claims);
        return new SuccessLogin(accessToken, 900);
    }
}
