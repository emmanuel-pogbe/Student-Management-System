package com.studentsystem.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class StudentProfile {
    private String level;
    private String department;

    @OneToOne
    private User user;
}
