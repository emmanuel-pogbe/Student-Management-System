package com.studentsystem.service.impl;

import com.studentsystem.dto.response.StudentListResponse;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.dto.response.TeacherListResponse;
import com.studentsystem.enums.RoleEnum;
import com.studentsystem.models.Organization;
import com.studentsystem.models.User;
import com.studentsystem.repository.OrganizationRepository;
import com.studentsystem.repository.UserRepository;
import com.studentsystem.service.interfaces.ChancellorService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChancellorServiceImpl implements ChancellorService {
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    public ChancellorServiceImpl(OrganizationRepository organizationRepository, UserRepository userRepository) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public SuccessResponse approveTeacherApplication(String emailOfTeacher) {
        // Add some validation here later
        Optional<User> gottenUser = userRepository.findByEmailAndUserRole(emailOfTeacher, RoleEnum.TEACHER);
        if (gottenUser.isEmpty()) {
            throw new RuntimeException("Teacher Not Found");
        }
        User teacher = gottenUser.get();
        if (teacher.isVerified()) {
            throw new RuntimeException("Teacher Already Verified");
        }
        teacher.setVerified(true);
        userRepository.save(teacher);
        return new SuccessResponse("Teacher has been approved and can now create courses");
    }

    @Override
    public SuccessResponse approveStudentApplication(String emailOfStudent) {
        Optional<User> gottenUser = userRepository.findByEmailAndUserRole(emailOfStudent, RoleEnum.STUDENT);
        if (gottenUser.isEmpty()) {
            throw new RuntimeException("Student Not Found");
        }
        User student = gottenUser.get();
        if (student.isVerified()) {
            throw new RuntimeException("Student Already Verified");
        }
        student.setVerified(true);
        userRepository.save(student);
        return new SuccessResponse("Student has been approved and can now register courses");
    }

    @Override
    public List<TeacherListResponse> getAllTeachers(Authentication authentication) {
        Organization organization = getOrganizationFromChancellor(authentication);
        List<User> teachers = userRepository.findByOrganizationAndUserRole(organization, RoleEnum.TEACHER);
        List<TeacherListResponse> teacherListResponses = new ArrayList<>();
        for (User teacher : teachers) {
            teacherListResponses.add(new TeacherListResponse(
                    teacher.getEmail(),
                    teacher.getFullName(),
                    teacher.isVerified()
            ));
        }

        return teacherListResponses;
    }

    @Override
    public List<TeacherListResponse> getAllTeachers(Authentication authentication, Boolean isVerified) {
        Organization organization = getOrganizationFromChancellor(authentication);
        List<User> teachers = userRepository.findByOrganizationAndUserRoleAndVerified(organization, RoleEnum.TEACHER,isVerified);
        List<TeacherListResponse> teacherListResponses = new ArrayList<>();
        for (User teacher : teachers) {
            teacherListResponses.add(new TeacherListResponse(
                    teacher.getEmail(),
                    teacher.getFullName(),
                    teacher.isVerified()
            ));
        }

        return teacherListResponses;
    }


    @Override
    public List<StudentListResponse> getAllStudents(Authentication authentication) {
        Organization organization = getOrganizationFromChancellor(authentication);
        List<User> students = userRepository.findByOrganizationAndUserRole(organization, RoleEnum.STUDENT);
        List<StudentListResponse> studentListResponses = new ArrayList<>();
        for (User student : students) {
            studentListResponses.add(new StudentListResponse(
                    student.getEmail(),
                    student.getFullName(),
                    student.isVerified()
            ));
        }
        return studentListResponses;
    }


    @Override
    public List<StudentListResponse> getAllStudents(Authentication authentication, Boolean isVerified) {
        Organization organization = getOrganizationFromChancellor(authentication);
        List<User> students = userRepository.findByOrganizationAndUserRoleAndVerified(organization, RoleEnum.STUDENT, isVerified);
        List<StudentListResponse> studentListResponses = new ArrayList<>();
        for (User student : students) {
            studentListResponses.add(new StudentListResponse(
                    student.getEmail(),
                    student.getFullName(),
                    student.isVerified()
            ));
        }
        return studentListResponses;
    }

    private Organization getOrganizationFromChancellor(Authentication authentication) {
        String email = authentication.getName();
        Optional<User> tempChancellor =  userRepository.findByEmailAndUserRole(email,RoleEnum.CHANCELLOR);
        // some verification should occur
        if (tempChancellor.isEmpty()) {
            throw new RuntimeException("Chancellor Not Found");
        }
        User chancellor = tempChancellor.get();
        Optional<Organization> org = organizationRepository.findByOwnedBy(chancellor);
        if (org.isEmpty()) {
            throw new RuntimeException("Organization Not Found");
        }
        Organization organization = org.get();
        if (!organization.isVerified()) {
            throw new RuntimeException("Organization Not Verified");
        }
        return organization;
    }
}
