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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        log.info("Email sent to {}",to);
    }

    @KafkaListener(topics = "student_notification", groupId = "studentgroup")
    public void listenToEmailMessages(CourseResourceAlertEmail courseResourceAlertEmail) {
        log.info("Sending email to all students offering {}", courseResourceAlertEmail.getTitle());

        List<User> allStudentsOfferingCourse;
        try {
            allStudentsOfferingCourse = studentCourseRepository.findAllStudentsByCourseId(courseResourceAlertEmail.getCourseId());
        } catch (Exception e) {
            log.error("Failed to load enrolled students for courseId={}", courseResourceAlertEmail.getCourseId(), e);
            return;
        }

        int sentCount = 0;
        int failedCount = 0;
        Set<String> notifiedEmails = new HashSet<>();

        for (User user : allStudentsOfferingCourse) {
            String studentEmail = user.getEmail();
            if (studentEmail == null || studentEmail.isBlank()) {
                failedCount++;
                log.warn("Skipping user with empty email. userId={}", user.getUserId());
                continue;
            }
            String normalizedEmail = studentEmail.trim().toLowerCase();
            if (!notifiedEmails.add(normalizedEmail)) {
                log.warn("Skipping duplicate recipient in same batch: {}", normalizedEmail);
                continue;
            }
            try {
                this.sendEmail(normalizedEmail, courseResourceAlertEmail.getTitle(), courseResourceAlertEmail.getMessage());
                sentCount++;
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                failedCount++;
                log.error("Listener interrupted, idk why", e);
                break;
            } catch (Exception e) {
                failedCount++;
                log.error("Failed to send email to {}", normalizedEmail, e);
            }
        }

        log.info("Done sending emails for {}. sent={}, failed={}", courseResourceAlertEmail.getTitle(), sentCount, failedCount);
    }
}
