package com.studentsystem.models.user;

import com.studentsystem.models.User;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Admin extends User {
    private Long adminId;
}
