package com.dev.calendara.apply.service;

import com.dev.calendara.apply.repository.ApplyRepository;
import com.dev.calendara.appointment.repository.AppointmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ApplyServiceTest {

    @Autowired
    private ApplyRepository applyRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @DisplayName("사용자가 미팅 정보를 작성 후 신청한다.")
    @Test
    void applyAppointment() {
        
    }
}