package com.dev.calendara.appointment.service;

import com.dev.calendara.appointment.controller.dto.AppointmentCreateRequest;
import com.dev.calendara.appointment.controller.dto.AvailableTimeRequest;
import com.dev.calendara.appointment.service.dto.AppointmentCreateResponse;
import com.dev.calendara.common.exception.custom.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        LocalDateTime startTime1 = LocalDateTime.of(2023, 7, 21, 0, 0);
        LocalDateTime endTime1 = LocalDateTime.of(2023, 7, 21, 1, 30);
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
        LocalDateTime startTime3 = LocalDateTime.of(2023, 7, 25, 23, 0);
        LocalDateTime endTime3 = LocalDateTime.of(2023, 7, 25, 23, 50);
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

    @Test
    @DisplayName("약속 신청에 대한 정보 중 미팅 기간에 대한 정보를 잘못 입력한 경우 예외가 발생한다.")
    void createAppointment2() {
        // Given
        LocalDateTime startTime1 = LocalDateTime.of(2023, 7, 21, 14, 0);
        LocalDateTime endTime1 = LocalDateTime.of(2023, 7, 21, 18, 30);
        AvailableTimeRequest availableTime1 = new AvailableTimeRequest(
                startTime1,
                endTime1
        );

        LocalDate meetingStartDate = LocalDate.of(2023, 7, 30);
        LocalDate meetingEndDate = LocalDate.of(2023, 7, 20);
        AppointmentCreateRequest createRequest = new AppointmentCreateRequest(
                "test title",
                1L,
                30,
                meetingStartDate,
                meetingEndDate,
                List.of(
                        availableTime1
                )
        );

        // When Then
        assertThatThrownBy(() -> appointmentService.createAppointment(createRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("회의 신청 기간을 잘못 설정했습니다.");
    }

    @Test
    @DisplayName("약속 신청에 대한 정보 중 미팅 가능 시간에 대한 정보를 잘못 입력한 경우 예외가 발생한다.")
    void createAppointment3() {
        // Given
        LocalDateTime startTime1 = LocalDateTime.of(2023, 7, 21, 14, 0);
        LocalDateTime endTime1 = LocalDateTime.of(2023, 7, 21, 18, 30);
        AvailableTimeRequest availableTime1 = new AvailableTimeRequest(
                startTime1,
                endTime1
        );
        LocalDateTime startTime2 = LocalDateTime.of(2023, 7, 19, 14, 0);
        LocalDateTime endTime2 = LocalDateTime.of(2023, 7, 19, 22, 30);
        AvailableTimeRequest impossibleTime = new AvailableTimeRequest(
                startTime2,
                endTime2
        );

        LocalDate meetingStartDate = LocalDate.of(2023, 7, 20);
        LocalDate meetingEndDate = LocalDate.of(2023, 7, 30);
        AppointmentCreateRequest createRequest = new AppointmentCreateRequest(
                "test title",
                1L,
                30,
                meetingStartDate,
                meetingEndDate,
                List.of(
                        availableTime1,
                        impossibleTime
                )
        );

        // When Then
        assertThatThrownBy(() -> appointmentService.createAppointment(createRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("미팅 가능한 시간대는 미팅 기간내에 포함되어야 합니다.");
    }
}
