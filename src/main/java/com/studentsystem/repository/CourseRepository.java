package com.studentsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studentsystem.models.Course;

public interface CourseRepository extends JpaRepository<Course,Long> {
}