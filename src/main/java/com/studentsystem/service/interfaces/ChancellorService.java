package com.studentsystem.service.interfaces;

import com.studentsystem.dto.response.StudentListResponse;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.dto.response.TeacherListResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ChancellorService {
    SuccessResponse approveTeacherApplication(String emailOfTeacher);
    SuccessResponse approveStudentApplication(String emailOfStudent);

    List<TeacherListResponse> getAllTeachers(Authentication authentication);
    List<TeacherListResponse> getAllTeachers(Authentication authentication, Boolean isVerified);

    List<StudentListResponse> getAllStudents(Authentication authentication);
    List<StudentListResponse> getAllStudents(Authentication authentication, Boolean isVerified);
}
