package com.studentsystem.models;

import com.studentsystem.models.user.Chancellor;
import com.studentsystem.models.user.Student;
import com.studentsystem.models.user.Teacher;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationNumber;
    private String name;
    private String address;

    private boolean isVerified;


    // one chancellor can create one organization
    @OneToOne
    private Chancellor chancellor;

//    @OneToMany
//    private List<Teacher> teachers;
//
//    @OneToMany
//    private List<Student> students;

}
