package com.studentsystem.repository;

import com.studentsystem.models.Course;
import com.studentsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.studentsystem.models.StudentCourse;

import java.util.List;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    List<StudentCourse> findByUser(User student);

    List<StudentCourse> findByCourse(Course course);

    @Query("select distinct sc.user from StudentCourse sc where sc.course = :course")
    List<User> findAllStudentsByCourse(@Param("course") Course course);
}
