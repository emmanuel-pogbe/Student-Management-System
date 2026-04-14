package com.studentsystem.service.impl;

import com.studentsystem.dto.request.CourseRegisterRequest;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.enums.RoleEnum;
import com.studentsystem.models.Course;
import com.studentsystem.models.Organization;
import com.studentsystem.models.StudentCourse;
import com.studentsystem.models.User;
import com.studentsystem.repository.CourseRepository;
import com.studentsystem.repository.OrganizationRepository;
import com.studentsystem.repository.StudentCourseRepository;
import com.studentsystem.repository.UserRepository;
import com.studentsystem.service.interfaces.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentCourseRepository studentCourseRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;
    public StudentServiceImpl(OrganizationRepository organizationRepository, UserRepository userRepository, CourseRepository courseRepository, PasswordEncoder passwordEncoder, StudentCourseRepository studentCourseRepository) {
        this.organizationRepository = organizationRepository;
        this.studentCourseRepository = studentCourseRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SuccessResponse requestJoinOrganization(String organizationNumber, Authentication authentication) {
        Optional<Organization> orgToJoin = organizationRepository.findByRegistrationNumber(organizationNumber);
        if (orgToJoin.isEmpty()) {
            throw new RuntimeException("Organization not found");
        }
        Organization org = orgToJoin.get();
        if (!org.isVerified()) {
            throw new RuntimeException("Organization is not verified");
        }
        Optional<User> student = userRepository.findByEmailAndUserRole(authentication.getName(),RoleEnum.STUDENT);
        if (student.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        User stud = student.get();
        if (stud.getOrganization() != null) {
            throw new RuntimeException("Already have an organization");
        }
        stud.setOrganization(org);
        stud.setVerified(false);
        userRepository.save(stud);
        return new SuccessResponse("Student requested join successfully, Awaiting verification from Chancellor");
    }

    @Override
    public SuccessResponse registerCourse(CourseRegisterRequest courseRegisterRequest, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> stud = userRepository.findByEmailAndUserRole(email, RoleEnum.STUDENT);
        if (stud.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        User student = stud.get();
        if (!student.isVerified()) {
            throw new RuntimeException("Student is not verified");
        }
        Optional<Course> courseToAdd = courseRepository.findByCourseCodeAndOrganization(courseRegisterRequest.getCourseCode(),student.getOrganization());
        if (courseToAdd.isEmpty()) {
            throw new RuntimeException("Course not found in Organization");
        }
        Course course = courseToAdd.get();
        if (!passwordEncoder.matches(courseRegisterRequest.getCoursePassword(),course.getCoursePassword())) {
            throw new RuntimeException("Wrong course password");
        }
        try {
            StudentCourse studentCourse = new StudentCourse(student,course);
            studentCourseRepository.save(studentCourse);
        } catch (Exception e) {
            throw new RuntimeException("Course not added");
        }

        return new SuccessResponse("Course registered successfully");
    }
}
