package com.studentsystem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "courses_id")
    private Course course;

    @ManyToOne()
    @JoinColumn(name = "student_user_id")
    private User user;

    public StudentCourse(User user, Course course) {
        this.course = course;
        this.user = user;
    }
}
