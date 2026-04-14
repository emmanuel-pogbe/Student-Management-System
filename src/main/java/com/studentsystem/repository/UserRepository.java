package com.studentsystem.repository;

import java.util.List;
import java.util.Optional;

import com.studentsystem.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentsystem.models.Organization;
import com.studentsystem.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndUserRole(String email, RoleEnum role);

    List<User> findByOrganizationAndUserRole(Organization organization, RoleEnum role);

    List<User> findByOrganizationAndUserRoleAndVerified(Organization organization, RoleEnum role, Boolean verified);
}
