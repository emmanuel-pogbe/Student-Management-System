package com.studentsystem.service.impl;

import com.studentsystem.dto.request.CourseResourceCreate;
import com.studentsystem.dto.response.CourseResourceResponse;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.models.Course;
import com.studentsystem.models.CourseResource;
import com.studentsystem.models.Organization;
import com.studentsystem.models.User;
import com.studentsystem.models.user.Student;
import com.studentsystem.models.user.Teacher;
import com.studentsystem.repository.CourseRepository;
import com.studentsystem.repository.CourseResourceRepository;
import com.studentsystem.repository.TeacherRepository;
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
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseResourceRepository courseResourceRepository;

    public CourseResourceServiceImpl(TeacherRepository teacherRepository, UserRepository userRepository, CourseRepository courseRepository, CourseResourceRepository courseResourceRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.courseResourceRepository = courseResourceRepository;
    }

    @Override
    public SuccessResponse createCourseResource(String courseCode, CourseResourceCreate courseResourceCreate, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        if (!user.get().getRole().equals("TEACHER")) {
            throw new RuntimeException("User is not teacher");
        }
        Teacher teach = (Teacher) user.get();
        Optional<Course> course = courseRepository.findByCourseCodeAndOrganization(courseCode, teach.getOrganization());
        if (course.isEmpty()) {
            throw new RuntimeException("Course not found in this organization");
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
        if (user.get().getRole().equals("TEACHER")) {
            Teacher teach = (Teacher) user.get();
            organization = teach.getOrganization();
        }
        else if (user.get().getRole().equals("STUDENT")) {
            Student stud = (Student) user.get();
            List<Course> registeredCourses = stud.getCourses();
            boolean found = false;
            for (Course course : registeredCourses) {
                if (course.getCourseCode().equals(courseCode)) {
                    found = true;
                }
            }
            if (!found) {
                throw new RuntimeException("You are not registered in this course");
            }
            organization = stud.getOrganization();
        }
        else {
            throw new RuntimeException("User is not a valid actor");
        }
        return organization;
    }
}
