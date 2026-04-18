package com.studentsystem.service;


import com.studentsystem.dto.request.UserCreate;
import com.studentsystem.dto.response.SuccessUserCreated;
import com.studentsystem.enums.RoleEnum;
import com.studentsystem.exception.custom.EmailAlreadyInUseException;
import com.studentsystem.mapper.UserMapper;
import com.studentsystem.models.StudentProfile;
import com.studentsystem.models.User;
import com.studentsystem.repository.StudentProfileRepository;
import com.studentsystem.repository.UserRepository;
import com.studentsystem.service.impl.UserServiceImpl;
import com.studentsystem.utils.JwtUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserServiceImpl.class)
public class UserServiceImplTest {
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private StudentProfileRepository studentProfileRepository;
    @MockitoBean
    private PasswordEncoder passwordEncoder;
    @MockitoBean
    private JwtUtils jwtUtils;
    @MockitoBean
    private UserMapper userMapper;

        @Autowired
    private UserServiceImpl userService;

        @Value("${studentapplication.password}")
        private String password;

    @Nested
    @DisplayName("Testing Create user method")
    class CreateUserMethod {
        UserCreate testCreatedUser;
        User userModel;
        @BeforeEach
        void setUp() {
            testCreatedUser = new UserCreate(
                    "test@gmail.com",
                    "12345",
                    "Test User 1",
                    RoleEnum.TEACHER
            );
            userModel = new User(
                    "testUser@gmail.com",
                    "encoded password",
                    "Pogbe Emmanuel",
                    RoleEnum.STUDENT,
                    LocalDateTime.now(),
                    true
            );
            when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        }

        @Test
        @DisplayName("What happens on a successful Student user creation attempt")
        void whatHappensSuccessfulUserCreation() {
            testCreatedUser.setUserRole(RoleEnum.STUDENT);
            testCreatedUser.setEmail("test@gmail.com");
            testCreatedUser.setLevel("300 Level");
            testCreatedUser.setDepartment("Test Department 1");
            when(userRepository.findByEmail("test@gmail.com"))
                    .thenReturn(Optional.empty());
            when(userRepository.save(any(User.class)))
                    .thenReturn(userModel);
            when(studentProfileRepository.save(any()))
                    .thenReturn(new StudentProfile("300 Level","Test department 1"));
            when(userMapper.dtoToModel(any()))
                    .thenReturn(userModel);

            // When
            SuccessUserCreated response = Assertions.assertDoesNotThrow(() -> userService.createUser(testCreatedUser));

            // Then
            Assertions.assertNotNull(response);
            Assertions.assertEquals("test@gmail.com",response.getEmail());
            verify(userRepository,times(1)).save(any(User.class));
            verify(studentProfileRepository,times(1)).save(any(StudentProfile.class));
        }

        @Test
        @DisplayName("What happens on a successful Admin user creation attempt")
        void whatHappensSuccessfulAdminCreation() {


            testCreatedUser.setUserRole(RoleEnum.ADMIN);
            testCreatedUser.setEmail("test@gmail.com");
            testCreatedUser.setLevel("300 Level");
            testCreatedUser.setDepartment("Test Department 1");
            testCreatedUser.setApplicationPassword(password);
            when(userRepository.findByEmail("test@gmail.com"))
                    .thenReturn(Optional.empty());
            when(userRepository.save(any(User.class)))
                    .thenReturn(userModel);
            when(studentProfileRepository.save(any()))
                    .thenReturn(new StudentProfile("300 Level","Test department 1"));
            when(userMapper.dtoToModel(any()))
                    .thenReturn(userModel);

            // When
            SuccessUserCreated response = Assertions.assertDoesNotThrow(() -> userService.createUser(testCreatedUser));

            // Then
            Assertions.assertNotNull(response);
            Assertions.assertEquals("test@gmail.com",response.getEmail());
            verify(userRepository,times(1)).save(any(User.class));
                        verify(studentProfileRepository,never()).save(any(StudentProfile.class));
        }

        @Test
        @DisplayName("What happens a teacher create attempt occurs with missing arguments")
        void shouldRaiseErrorOnTeacherCreateWithMissingParameters() {
            // Given & When & Then
                        Assertions.assertThrows(InvalidParameterException.class, () -> {
                userService.createUser(testCreatedUser);
            });
        }

        @Test
        @DisplayName("What happens when email not found in User Create")
        void whatHappensWhenRoleIsNullOrInvalid() {
            // Given
            testCreatedUser.setUserRole(null);
            when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
            // When
            RuntimeException runtimeExceptionForNullRole = Assertions.assertThrows(RuntimeException.class, () -> {userService.createUser(testCreatedUser);});

            // Then
            Assertions.assertEquals("User role is not valid", runtimeExceptionForNullRole.getMessage());
        }

        @Test
        @DisplayName("What happens when email is present")
        void whatHappensWhenEmailAlreadyExists() {
            // Given
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userModel));

            // When
            EmailAlreadyInUseException exceptionForEmail = Assertions.assertThrows(EmailAlreadyInUseException.class,()->{userService.createUser(testCreatedUser);});

            // Then
            Assertions.assertEquals("Email already exists", exceptionForEmail.getMessage());
        }

        @Test
        @DisplayName("What happens when you try to create an admin user without a password")
        void whatHappensWhenUserIsAdminWithoutPassword() {
            // Given
            testCreatedUser.setUserRole(RoleEnum.ADMIN);
            when(userRepository.findByEmail(anyString()))
                    .thenReturn(Optional.empty());

            // When
            IllegalArgumentException noPassword = Assertions.assertThrows(IllegalArgumentException.class, () -> {userService.createUser(testCreatedUser);});

            // Then
            Assertions.assertEquals("You can't create an admin user", noPassword.getMessage());
        }
    }

}
