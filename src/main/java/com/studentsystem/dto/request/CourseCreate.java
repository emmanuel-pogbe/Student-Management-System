package com.studentsystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCreate {
    private String courseCode;
    private String courseName;
    private String coursePassword;
}
