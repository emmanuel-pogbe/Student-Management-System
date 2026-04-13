package com.studentsystem.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class CourseResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resourceTitle;
    private String resource;

    private LocalDateTime createdAt;

    @ManyToOne
    private Course course;
}
