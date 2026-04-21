package com.studentsystem.models;

import jakarta.persistence.*;
import lombok.Data;

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
    @JoinColumn(name = "chancellor_id")
    private User ownedBy;

//    @OneToMany
//    private List<Teacher> teachers;

//
//    @OneToMany
//    private List<Student> students;

}
