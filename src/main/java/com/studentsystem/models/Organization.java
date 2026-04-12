package com.studentsystem.models;

import com.studentsystem.models.user.Chancellor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Organization {
    @Id
    private Long id;

    private String name;
    private String address;

    private boolean isVerified;


    // one chancellor can create one organization
    @OneToOne
    private Chancellor chancellor;

}
