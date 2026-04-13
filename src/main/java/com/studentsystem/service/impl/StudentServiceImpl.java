package com.studentsystem.service.impl;

import com.studentsystem.dto.request.CourseRegisterRequest;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.models.Course;
import com.studentsystem.models.Organization;
import com.studentsystem.models.user.Student;
import com.studentsystem.models.user.Teacher;
import com.studentsystem.repository.CourseRepository;
import com.studentsystem.repository.OrganizationRepository;
import com.studentsystem.repository.StudentRepository;
import com.studentsystem.repository.TeacherRepository;
import com.studentsystem.service.interfaces.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final OrganizationRepository organizationRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;
    public StudentServiceImpl(OrganizationRepository organizationRepository, StudentRepository studentRepository, CourseRepository courseRepository, PasswordEncoder passwordEncoder) {
        this.organizationRepository = organizationRepository;
        this.studentRepository = studentRepository;
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
        Optional<Student> student = studentRepository.findByEmail(authentication.getName());
        if (student.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        Student stud = student.get();
        if (stud.getOrganization() != null) {
            throw new RuntimeException("Already have an organization");
        }
        stud.setOrganization(org);
        stud.setVerified(false);
        studentRepository.save(stud);
        return new SuccessResponse("Student requested join successfully, Awaiting verification from Chancellor");
    }

    @Override
    public SuccessResponse registerCourse(CourseRegisterRequest courseRegisterRequest, Authentication authentication) {
        String email = authentication.getName();
        Optional<Student> stud = studentRepository.findByEmail(email);
        if (stud.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        Student student = stud.get();
        Optional<Course> courseToAdd = courseRepository.findByCourseCodeAndOrganization(courseRegisterRequest.getCourseCode(),student.getOrganization());
        if (courseToAdd.isEmpty()) {
            throw new RuntimeException("Course not found in Organization");
        }
        Course course = courseToAdd.get();
        if (!passwordEncoder.matches(courseRegisterRequest.getCoursePassword(),course.getCoursePassword())) {
            throw new RuntimeException("Wrong course password");
        }
        if (student.getCourses() isinstanceOf List) {
            List<Course> currentCourses = student.getCourses();
        }
        else {
            List<Course> currentCourses = new ArrayList<>();
        }
        currentCourses.add(course);
        student.setCourses(currentCourses);
        studentRepository.save(student);

        return new SuccessResponse("Course registered successfully");
    }
}
