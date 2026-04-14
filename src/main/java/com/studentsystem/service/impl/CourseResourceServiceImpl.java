package com.studentsystem.service.impl;

import com.studentsystem.dto.request.CourseResourceCreate;
import com.studentsystem.dto.response.CourseResourceResponse;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.enums.RoleEnum;
import com.studentsystem.models.Course;
import com.studentsystem.models.CourseResource;
import com.studentsystem.models.Organization;
import com.studentsystem.models.User;
import com.studentsystem.repository.CourseRepository;
import com.studentsystem.repository.CourseResourceRepository;
import com.studentsystem.repository.UserRepository;
import com.studentsystem.service.interfaces.CourseResourceService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseResourceServiceImpl implements CourseResourceService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseResourceRepository courseResourceRepository;

    public CourseResourceServiceImpl(UserRepository userRepository, CourseRepository courseRepository, CourseResourceRepository courseResourceRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.courseResourceRepository = courseResourceRepository;
    }

    @Override
    public SuccessResponse createCourseResource(String courseCode, CourseResourceCreate courseResourceCreate, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmailAndUserRole(email,RoleEnum.TEACHER);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User teach = user.get();
        Optional<Course> course = courseRepository.findByCourseCodeAndOrganizationAndCreatedBy(courseCode, teach.getOrganization(),teach);
        if (course.isEmpty()) {
            throw new RuntimeException("Course not found or you don't own it");
        }
        CourseResource courseResource = new CourseResource();
        courseResource.setResourceTitle(courseResourceCreate.getResourceTitle());
        courseResource.setResource(courseResourceCreate.getResource());
        courseResource.setCreatedAt(LocalDateTime.now());
        courseResource.setCourse(course.get());
        courseResourceRepository.save(courseResource);
        return new SuccessResponse("Course resource successfully created");

    }

    @Override
    public List<CourseResourceResponse> getAllCourseResources(String courseCode, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        Organization organization = getOrganization(courseCode, user);
        Optional<Course> course = courseRepository.findByCourseCodeAndOrganization(courseCode,organization);
        if (course.isEmpty()) {
            throw new RuntimeException("Course not found in this organization");
        }
        List<CourseResource> courseResources = courseResourceRepository.findAllByCourse(course.get());
        List<CourseResourceResponse> courseResourceResponses = new ArrayList<>();
        for (CourseResource individualCourse: courseResources) {
            courseResourceResponses.add(new CourseResourceResponse(
                    individualCourse.getId(),
                    individualCourse.getResourceTitle(),
                    individualCourse.getResource(),
                    individualCourse.getCreatedAt()
            ));
        }

        return courseResourceResponses;
    }

    private static Organization getOrganization(String courseCode, Optional<User> user) {
        Organization organization;
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        if (List.of("TEACHER","CHANCELLOR","STUDENT").contains(user.get().getUserRole().name())) {
            User userProfile =  user.get();
            organization = userProfile.getOrganization();
        }
        else {
            throw new RuntimeException("User is not a valid actor");
        }
        return organization;
    }
}
