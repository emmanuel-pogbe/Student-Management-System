package com.studentsystem.repository;

import com.studentsystem.models.Organization;
import com.studentsystem.models.user.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByEmail(String email);


    List<Teacher> findByOrganization(Organization organization);

    @Query("SELECT t FROM Teacher t WHERE t.organization = :organization AND t.isVerified = :isVerified")
    List<Teacher> findByOrganizationAndIsVerified(
        @Param("organization") Organization organization,
        @Param("isVerified") Boolean isVerified
    );
}
