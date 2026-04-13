package com.studentsystem.repository;

import com.studentsystem.models.Course;
import com.studentsystem.models.CourseResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseResourceRepository extends JpaRepository<CourseResource, Long> {
    List<CourseResource> findAllByCourse(Course course);
}
