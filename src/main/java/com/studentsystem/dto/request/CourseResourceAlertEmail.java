package com.studentsystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResourceAlertEmail {
    private String title;
    private String message;
    private Long courseId;
}
