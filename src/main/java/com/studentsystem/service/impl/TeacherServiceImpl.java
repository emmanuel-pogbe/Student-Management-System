package com.studentsystem.service.impl;

import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.enums.RoleEnum;
import com.studentsystem.models.Organization;
import com.studentsystem.models.User;
import com.studentsystem.repository.OrganizationRepository;
import com.studentsystem.repository.UserRepository;
import com.studentsystem.service.interfaces.KafkaService;
import com.studentsystem.service.interfaces.TeacherService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    public TeacherServiceImpl(UserRepository userRepository, OrganizationRepository organizationRepository) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public SuccessResponse requestJoinOrganization(String organizationNumber, Authentication authentication) {
        Optional<Organization> orgToJoin = organizationRepository.findByRegistrationNumber(organizationNumber);
        if (orgToJoin.isEmpty()) {
            throw new RuntimeException("Organization not found");
        }
        Organization org = orgToJoin.get();
        if (!org.isVerified()) {
            throw new RuntimeException("Organization is not verified");
        }
        Optional<User> teacher = userRepository.findByEmailAndUserRole(authentication.getName(),RoleEnum.TEACHER);
        if (teacher.isEmpty()) {
            throw new RuntimeException("Teacher not found");
        }
        User teach = teacher.get();
        if (teach.getOrganization() != null) {
            throw new RuntimeException("Already have an organization");
        }
        teach.setOrganization(org);
        teach.setVerified(false);
        userRepository.save(teach);
        return new SuccessResponse("Teacher requested join successfully, Awaiting verification from Chancellor");
    }
}
