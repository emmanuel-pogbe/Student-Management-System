package com.studentsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResourceResponse {
    private Long id;
    private String resourceTitle;
    private String resource;

    private LocalDateTime createdAt;
}
