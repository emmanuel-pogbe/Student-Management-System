package com.studentsystem.service.interfaces;

public interface EmailNotificationService {
    void sendEmail(String to, String subject, String body);
}
