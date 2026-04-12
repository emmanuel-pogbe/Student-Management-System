package com.studentsystem.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CourseResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resource;

    @ManyToOne
    private Course course;
}
