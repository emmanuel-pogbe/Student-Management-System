package com.studentsystem.repository;

import com.studentsystem.models.user.Chancellor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChancellorRepository extends JpaRepository<Chancellor, Long> {

    Optional<Chancellor> findByEmail(String email);
}
