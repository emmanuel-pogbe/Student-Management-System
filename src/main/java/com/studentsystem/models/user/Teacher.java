package com.studentsystem.models.user;

import com.studentsystem.models.Organization;
import com.studentsystem.models.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Teacher extends User {
    private String department;
    private String specialty;

    //Many teachers can join one organization
    @ManyToOne
    private Organization organization;


    // chancellor can approve teachers
    private boolean isVerified;
}