package com.studentsystem.repository;

import com.studentsystem.enums.RoleEnum;
import com.studentsystem.mapper.UserMapper;
import com.studentsystem.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "jwt.secret=0123456789012345678901234567890123456789012345678901234567890123",
    "studentapplication.password=12345",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class UserRepositoryTest {

    @MockitoBean
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByEmail returns matching user")
    void findByEmailShouldReturnSavedUser() {
        User user = new User(
            "repo_user@test.com",
            "encoded-password",
            "Repo User",
            RoleEnum.STUDENT,
            LocalDateTime.now(),
            true
        );
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("repo_user@test.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("repo_user@test.com");
        assertThat(result.get().getUserRole()).isEqualTo(RoleEnum.STUDENT);
    }

    @Test
    @DisplayName("findByEmailAndUserRole returns user only when role matches")
    void findByEmailAndUserRoleShouldFilterByRole() {
        User user = new User(
            "role_user@test.com",
            "encoded-password",
            "Role User",
            RoleEnum.TEACHER,
            LocalDateTime.now(),
            true
        );
        userRepository.save(user);

        Optional<User> matching = userRepository.findByEmailAndUserRole("role_user@test.com", RoleEnum.TEACHER);
        Optional<User> nonMatching = userRepository.findByEmailAndUserRole("role_user@test.com", RoleEnum.STUDENT);

        assertThat(matching).isPresent();
        assertThat(nonMatching).isEmpty();
    }
}
