package com.studentsystem.service.impl;

import com.studentsystem.dto.request.CourseResourceAlertEmail;
import com.studentsystem.service.interfaces.KafkaService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService {
    private KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public void sendMessage(CourseResourceAlertEmail email) {
        kafkaTemplate.send("student_notification", email);
    }
}
