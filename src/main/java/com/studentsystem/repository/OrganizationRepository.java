package com.studentsystem.repository;

import com.studentsystem.models.user.Chancellor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.studentsystem.models.Organization;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long>{

    Optional<Organization> findByChancellor(Chancellor chancellor);
    Optional<Organization> findByRegistrationNumber(String name);

    @Query("SELECT org FROM Organization org WHERE org.isVerified=false")
    List<Organization> findPendingOrganizationRequests();
}
