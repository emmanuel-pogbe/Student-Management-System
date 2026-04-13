package com.studentsystem.repository;

import com.studentsystem.models.Organization;
import com.studentsystem.models.user.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByEmail(String email);


    List<Teacher> findByOrganization(Organization organization);
    List<Teacher> findByOrganizationAndVerified(Organization organization, boolean verified);
}
