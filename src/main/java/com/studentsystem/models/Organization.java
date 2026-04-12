package com.studentsystem.models;

import com.studentsystem.models.user.Chancellor;
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
    private Chancellor chancellor;

}
