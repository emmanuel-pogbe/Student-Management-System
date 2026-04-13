package com.studentsystem.repository;

import com.studentsystem.models.Organization;
import com.studentsystem.models.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);

    List<Student> findByOrganization(Organization organization);
    List<Student> findByOrganizationAndVerified(Organization organization, boolean verified);
}

