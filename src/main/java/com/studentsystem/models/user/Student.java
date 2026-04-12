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
public class Student extends User {
    private String level;
    private String department;

    @ManyToOne
    private Organization organization;
}
