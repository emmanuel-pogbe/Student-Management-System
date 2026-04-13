package com.studentsystem.service.impl;

import com.studentsystem.dto.response.StudentListResponse;
import com.studentsystem.dto.response.SuccessResponse;
import com.studentsystem.dto.response.TeacherListResponse;
import com.studentsystem.models.Organization;
import com.studentsystem.models.user.Chancellor;
import com.studentsystem.models.user.Student;
import com.studentsystem.models.user.Teacher;
import com.studentsystem.repository.ChancellorRepository;
import com.studentsystem.repository.OrganizationRepository;
import com.studentsystem.repository.StudentRepository;
import com.studentsystem.repository.TeacherRepository;
import com.studentsystem.service.interfaces.ChancellorService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChancellorServiceImpl implements ChancellorService {
    private final TeacherRepository teacherRepository;
    private final ChancellorRepository chancellorRepository;
    private final OrganizationRepository organizationRepository;
    private final StudentRepository studentRepository;

    public ChancellorServiceImpl(TeacherRepository teacherRepository, ChancellorRepository chancellorRepository, OrganizationRepository organizationRepository, StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.chancellorRepository = chancellorRepository;
        this.organizationRepository = organizationRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public SuccessResponse approveTeacherApplication(String emailOfTeacher) {
        // Add some validation here later
        Optional<Teacher> gottenUser = teacherRepository.findByEmail(emailOfTeacher);
        if (gottenUser.isEmpty()) {
            throw new RuntimeException("Teacher Not Found");
        }
        Teacher teacher = gottenUser.get();
        if (teacher.isVerified()) {
            throw new RuntimeException("Teacher Already Verified");
        }
        teacher.setVerified(true);
        teacherRepository.save(teacher);
        return new SuccessResponse("Teacher has been approved and can now create courses");
    }

    @Override
    public SuccessResponse approveStudentApplication(String emailOfStudent) {
        Optional<Student> gottenUser = studentRepository.findByEmail(emailOfStudent);
        if (gottenUser.isEmpty()) {
            throw new RuntimeException("Student Not Found");
        }
        Student student = gottenUser.get();
        if (student.isVerified()) {
            throw new RuntimeException("Student Already Verified");
        }
        student.setVerified(true);
        studentRepository.save(student);
        return new SuccessResponse("Student has been approved and can now register courses");
    }

    @Override
    public List<TeacherListResponse> getAllTeachers(Authentication authentication) {
        Organization organization = getOrganizationFromChancellor(authentication);
        List<Teacher> teachers = teacherRepository.findByOrganization(organization);
        List<TeacherListResponse> teacherListResponses = new ArrayList<>();
        for (Teacher teacher : teachers) {
            teacherListResponses.add(new TeacherListResponse(
                    teacher.getEmail(),
                    teacher.getFullName(),
                    teacher.getDepartment(),
                    teacher.getSpecialty(),
                    teacher.isVerified()
            ));
        }

        return teacherListResponses;
    }

    @Override
    public List<TeacherListResponse> getAllTeachers(Authentication authentication, Boolean isVerified) {
        Organization organization = getOrganizationFromChancellor(authentication);
        List<Teacher> teachers = teacherRepository.findByOrganizationAndVerified(organization,isVerified);
        List<TeacherListResponse> teacherListResponses = new ArrayList<>();
        for (Teacher teacher : teachers) {
            teacherListResponses.add(new TeacherListResponse(
                    teacher.getEmail(),
                    teacher.getFullName(),
                    teacher.getDepartment(),
                    teacher.getSpecialty(),
                    teacher.isVerified()
            ));
        }

        return teacherListResponses;
    }


    @Override
    public List<StudentListResponse> getAllStudents(Authentication authentication) {
        Organization organization = getOrganizationFromChancellor(authentication);
        List<Student> students = studentRepository.findByOrganization(organization);
        List<StudentListResponse> studentListResponses = new ArrayList<>();
        for (Student student : students) {
            studentListResponses.add(new StudentListResponse(
                    student.getEmail(),
                    student.getFullName(),
                    student.getLevel(),
                    student.getDepartment(),
                    student.isVerified()
            ));
        }
        return studentListResponses;
    }


    @Override
    public List<StudentListResponse> getAllStudents(Authentication authentication, Boolean isVerified) {
        Organization organization = getOrganizationFromChancellor(authentication);
        List<Student> students = studentRepository.findByOrganizationAndVerified(organization,isVerified);
        List<StudentListResponse> studentListResponses = new ArrayList<>();
        for (Student student : students) {
            studentListResponses.add(new StudentListResponse(
                    student.getEmail(),
                    student.getFullName(),
                    student.getLevel(),
                    student.getDepartment(),
                    student.isVerified()
            ));
        }
        return studentListResponses;
    }

    private Organization getOrganizationFromChancellor(Authentication authentication) {
        String email = authentication.getName();
        Optional<Chancellor> tempChancellor =  chancellorRepository.findByEmail(email);
        // some verification should occur
        if (tempChancellor.isEmpty()) {
            throw new RuntimeException("Chancellor Not Found");
        }
        Chancellor chancellor = tempChancellor.get();
        Optional<Organization> org = organizationRepository.findByChancellor(chancellor);
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
