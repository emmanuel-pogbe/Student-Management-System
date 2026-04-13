package com.studentsystem.models.user;

import com.studentsystem.models.Organization;
import com.studentsystem.models.Course;
import com.studentsystem.models.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Student extends User {
    private String level;
    private String department;

    private boolean isVerified;

    @ManyToOne
    private Organization organization;

    @ManyToMany
    private List<Course> courses;
}
