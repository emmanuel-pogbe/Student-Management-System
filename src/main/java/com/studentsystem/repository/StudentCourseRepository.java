package com.studentsystem.repository;

import com.studentsystem.models.Course;
import com.studentsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.studentsystem.models.StudentCourse;

import java.util.List;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    List<StudentCourse> findByUser(User student);

    List<StudentCourse> findByCourse(Course course);

    @Query()
    List<User> findAllStudentsByCourse(Course course);
}
