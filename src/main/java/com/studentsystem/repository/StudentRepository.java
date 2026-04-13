package com.studentsystem.repository;

import com.studentsystem.models.Organization;
import com.studentsystem.models.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);

    List<Student> findByOrganization(Organization organization);

    @Query("SELECT s FROM Student s WHERE s.organization = :organization AND s.isVerified = :isVerified")
    List<Student> findByOrganizationAndIsVerified(
        @Param("organization") Organization organization,
        @Param("isVerified") Boolean isVerified
    );
}

