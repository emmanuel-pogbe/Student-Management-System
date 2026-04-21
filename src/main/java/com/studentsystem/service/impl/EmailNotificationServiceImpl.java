package com.studentsystem.service.impl;

import com.studentsystem.dto.request.CourseResourceAlertEmail;
import com.studentsystem.models.User;
import com.studentsystem.repository.StudentCourseRepository;
import com.studentsystem.service.interfaces.EmailNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailNotificationServiceImpl implements EmailNotificationService {
    private JavaMailSender mailSender;

    private StudentCourseRepository studentCourseRepository;

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationServiceImpl.class);

    public EmailNotificationServiceImpl(JavaMailSender mailSender, StudentCourseRepository studentCourseRepository) {
        this.mailSender = mailSender;
        this.studentCourseRepository = studentCourseRepository;
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@moodle.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @KafkaListener(topics = "student_notification", groupId = "studentgroup")
    public void listenToEmailMessages(CourseResourceAlertEmail courseResourceAlertEmail) {
        log.info("Sending email to all students offering {}",courseResourceAlertEmail.getTitle());
        List<User> allStudentsOfferingCourse = studentCourseRepository.findAllStudentsByCourse(courseResourceAlertEmail.getCourse());
        for (User user : allStudentsOfferingCourse) {
            log.info("Pretending to send emails to students");
            String studentEmail = user.getEmail();
            this.sendEmail(studentEmail, courseResourceAlertEmail.getTitle(), courseResourceAlertEmail.getMessage());
        }
        log.info("Done sending emails to all students offering {}",courseResourceAlertEmail.getTitle());
    }
}
