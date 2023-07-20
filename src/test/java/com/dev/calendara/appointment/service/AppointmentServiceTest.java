package com.dev.calendara.appointment.service;

import com.dev.calendara.appointment.controller.dto.AppointmentCreateRequest;
import com.dev.calendara.appointment.controller.dto.AvailableTimeRequest;
import com.dev.calendara.appointment.service.dto.AppointmentCreateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
@SpringBootTest
class AppointmentServiceTest {

    @Autowired
    private AppointmentService appointmentService;

    @Test
    @DisplayName("약속 신청에 대한 정보(제목, 신청 가능기간 및 시간, 미팅 지속시간)를 받아 약속 신청을 생성한다.")
    void createAppointment() {
        // Given
        LocalDateTime startTime1 = LocalDateTime.of(2023, 7, 21, 14, 0);
        LocalDateTime endTime1 = LocalDateTime.of(2023, 7, 21, 18, 30);
        AvailableTimeRequest availableTime1 = new AvailableTimeRequest(
                startTime1,
                endTime1
        );
        LocalDateTime startTime2 = LocalDateTime.of(2023, 7, 22, 14, 0);
        LocalDateTime endTime2 = LocalDateTime.of(2023, 7, 22, 22, 30);
        AvailableTimeRequest availableTime2 = new AvailableTimeRequest(
                startTime2,
                endTime2
        );
        LocalDateTime startTime3 = LocalDateTime.of(2023, 7, 25, 9, 0);
        LocalDateTime endTime3 = LocalDateTime.of(2023, 7, 25, 20, 30);
        AvailableTimeRequest availableTime3 = new AvailableTimeRequest(
                startTime3,
                endTime3
        );

        LocalDate meetingStartDate = LocalDate.of(2023, 7, 21);
        LocalDate meetingEndDate = LocalDate.of(2023, 7, 25);
        AppointmentCreateRequest createRequest = new AppointmentCreateRequest(
                "test title",
                1L,
                30,
                meetingStartDate,
                meetingEndDate,
                List.of(
                        availableTime1,
                        availableTime2,
                        availableTime3
                )
        );

        // When
        AppointmentCreateResponse appointmentCreateResponse = appointmentService.createAppointment(createRequest);

        // Then
        assertThat(appointmentCreateResponse.appointmentId()).isNotNull();
        assertThat(appointmentCreateResponse.title()).isEqualTo("test title");
        assertThat(appointmentCreateResponse.meetingDuration()).isEqualTo(30);
        assertThat(appointmentCreateResponse.meetingStartDate()).isEqualTo(meetingStartDate);
        assertThat(appointmentCreateResponse.meetingEndDate()).isEqualTo(meetingEndDate);
        assertThat(appointmentCreateResponse.availableTimeResponses()).hasSize(3)
                .extracting("availableStartTime", "availableEndTime")
                .containsExactlyInAnyOrder(
                        tuple(startTime1, endTime1),
                        tuple(startTime2, endTime2),
                        tuple(startTime3, endTime3)
                );
    }
}
