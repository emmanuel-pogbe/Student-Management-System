package com.studentsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentsystem.models.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long>{    
    
}
