package com.studentsystem.service.impl;

import com.studentsystem.dto.request.CourseCreate;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.models.Course;
import com.studentsystem.models.User;
import com.studentsystem.models.user.Teacher;
import com.studentsystem.repository.CourseRepository;
import com.studentsystem.repository.UserRepository;
import com.studentsystem.service.interfaces.CourseService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CourseServiceImpl(CourseRepository courseRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SuccessResponse createCourse(CourseCreate courseCreate, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        if (!user.get().getRole().equals("TEACHER")) {
            throw new RuntimeException("User is not teacher");
        }
        Teacher teach = (Teacher) user.get();
        if (courseRepository.findByCourseCodeAndOrganization(courseCreate.getCourseCode(),teach.getOrganization()).isPresent()) {
            throw new RuntimeException("Course already exists");
        }
        Course course = new Course();
        course.setCourseCode(courseCreate.getCourseCode());
        course.setCourseName(courseCreate.getCourseName());
        course.setCoursePassword(passwordEncoder.encode(courseCreate.getCoursePassword()));
        course.setOrganization(teach.getOrganization());
        course.setTeacher(teach);
        courseRepository.save(course);
        return new SuccessResponse("Course created!");
    }
}
