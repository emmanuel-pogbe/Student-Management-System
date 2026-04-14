package com.studentsystem.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseCode;
    private String courseName;

    private String coursePassword;

    // A teacher can create multiple courses
    @ManyToOne
    private User createdBy;

    @ManyToOne
    private Organization organization;

}
