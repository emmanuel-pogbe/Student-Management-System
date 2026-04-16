package com.studentsystem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.studentsystem.enums.RoleEnum;

@Entity
@Table(name = "user_table")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String email;

    private String password;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private RoleEnum userRole; //ADMIN, CHANCELLOR, TEACHER, STUDENT

    private LocalDateTime created_at;

    @ManyToOne
    private Organization organization;

    private boolean isVerified;

    public User(String email, String password, String fullName, RoleEnum userRole, LocalDateTime created_at, boolean isVerified) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.userRole = userRole;
        this.created_at = created_at;
        this.isVerified = isVerified;
    }
}