package com.studentsystem.service.interfaces;

import com.studentsystem.dto.request.CourseResourceAlertEmail;

public interface KafkaService {
    void sendMessage(String message);

    void sendMessage(CourseResourceAlertEmail email);
}
