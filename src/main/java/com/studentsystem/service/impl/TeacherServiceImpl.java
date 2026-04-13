package com.studentsystem.service.impl;

import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.models.Organization;
import com.studentsystem.models.user.Teacher;
import com.studentsystem.repository.OrganizationRepository;
import com.studentsystem.repository.TeacherRepository;
import com.studentsystem.service.interfaces.TeacherService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final OrganizationRepository organizationRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository, OrganizationRepository organizationRepository) {
        this.teacherRepository = teacherRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public SuccessResponse requestJoinOrganization(String organizationNumber, Authentication authentication) {
        Optional<Organization> orgToJoin = organizationRepository.findByRegistrationNumber(organizationNumber);
        if (orgToJoin.isEmpty()) {
            throw new RuntimeException("Organization not found");
        }
        Organization org = orgToJoin.get();
        Optional<Teacher> teacher = teacherRepository.findByEmail(authentication.getName());
        if (teacher.isEmpty()) {
            throw new RuntimeException("Teacher not found");
        }
        Teacher teach = teacher.get();
        if (teach.getOrganization() != null) {
            throw new RuntimeException("Already have an organization");
        }
        teach.setOrganization(org);
        teach.setVerified(false);
        teacherRepository.save(teach);
        return new SuccessResponse("Teacher requested join successfully, Awaiting verification from Chancellor");
    }
}
