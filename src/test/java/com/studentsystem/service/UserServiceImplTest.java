package com.studentsystem.service;


import com.studentsystem.dto.request.UserCreate;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.dto.response.SuccessUserCreated;
import com.studentsystem.enums.RoleEnum;
import com.studentsystem.repository.StudentProfileRepository;
import com.studentsystem.repository.UserRepository;
import com.studentsystem.service.impl.UserServiceImpl;
import com.studentsystem.utils.JwtUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.InvalidParameterException;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private StudentProfileRepository studentProfileRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils;


    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    @DisplayName("Testing Create user method")
    class CreateUserMethod {
        UserCreate testCreatedUser;
        @BeforeEach
        void setUp() {
            testCreatedUser = new UserCreate(
                    "test@gmail.com",
                    "12345",
                    "Test User 1",
                    RoleEnum.TEACHER
            );
        }


        @Test
        void shouldRaiseErrorOnTeacherCreateWithMissingParameters() {
            // Given & When & Then
            InvalidParameterException invalidParameterException = Assertions.assertThrows(InvalidParameterException.class, () -> {
                userService.createUser(testCreatedUser);
            });
        }
    }

}
