package com.studentsystem.repository;

import com.studentsystem.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import com.studentsystem.models.Course;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course,Long> {
    Optional<Course> findByCourseCodeAndOrganization(String courseCode, Organization organization);
}