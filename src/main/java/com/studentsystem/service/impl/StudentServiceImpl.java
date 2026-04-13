package com.studentsystem.service.impl;

import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.models.Organization;
import com.studentsystem.models.user.Student;
import com.studentsystem.models.user.Teacher;
import com.studentsystem.repository.OrganizationRepository;
import com.studentsystem.repository.StudentRepository;
import com.studentsystem.repository.TeacherRepository;
import com.studentsystem.service.interfaces.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final OrganizationRepository organizationRepository;
    private final StudentRepository studentRepository;
    public StudentServiceImpl(OrganizationRepository organizationRepository, StudentRepository studentRepository) {
        this.organizationRepository = organizationRepository;
        this.studentRepository = studentRepository;
    }

    public SuccessResponse requestJoinOrganization(String organizationNumber, Authentication authentication) {
        Optional<Organization> orgToJoin = organizationRepository.findByRegistrationNumber(organizationNumber);
        if (orgToJoin.isEmpty()) {
            throw new RuntimeException("Organization not found");
        }
        Organization org = orgToJoin.get();
        Optional<Student> student = studentRepository.findByEmail(authentication.getName());
        if (student.isEmpty()) {
            throw new RuntimeException("Student not found");
        }
        Student stud = student.get();
        if (stud.getOrganization() != null) {
            throw new RuntimeException("Already have an organization");
        }
        stud.setOrganization(org);
        stud.setVerified(false);
        studentRepository.save(stud);
        return new SuccessResponse("Student requested join successfully, Awaiting verification from Chancellor");
    }
}
