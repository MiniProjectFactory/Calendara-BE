package com.dev.calendara.apply.service;

import com.dev.calendara.apply.service.dto.ApplyCreateServiceRequest;
import com.dev.calendara.apply.service.dto.ApplyCreateServiceResponse;
import com.dev.calendara.appointment.Appointment;
import com.dev.calendara.appointment.repository.AppointmentRepository;
import com.dev.calendara.availabletimes.AvailableTime;
import com.dev.calendara.common.exception.custom.BusinessException;
import com.dev.calendara.common.exception.dto.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ApplyServiceTest {

    @Autowired
    private ApplyService applyService;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @DisplayName("사용자가 미팅 정보를 작성 후 신청할 수 있다.")
    @Test
    void applyAppointment() {
        // given
        Appointment appointment = Appointment.builder()
                .hostId(1L)
                .meetingDuration(30)
                .meetingStartDate(LocalDate.of(2023, 8, 1))
                .meetingEndDate(LocalDate.of(2023, 8, 10))
                .title("test")
                .build();
        AvailableTime availableTime1 = AvailableTime.builder()
                .availableStartTime(LocalDateTime.of(2023, 8, 2, 18, 0))
                .availableEndTime(LocalDateTime.of(2023, 8, 2, 23, 0))
                .build();
        AvailableTime availableTime2 = AvailableTime.builder()
                .availableStartTime(LocalDateTime.of(2023, 8, 3, 18, 0))
                .availableEndTime(LocalDateTime.of(2023, 8, 3, 23, 0))
                .build();
        availableTime1.addAvailableTime(appointment);
        availableTime2.addAvailableTime(appointment);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        // when
        ApplyCreateServiceResponse applyCreateServiceResponse = applyService.applyAppointment(new ApplyCreateServiceRequest(1L, LocalDateTime.of(2023, 8, 2, 18, 0), LocalDateTime.of(2023, 8, 2, 18, 30), 1L));

        // then
        assertThat(applyCreateServiceResponse.applyId()).isNotNull();
    }

    @DisplayName("사용자가 미팅 신청 시 호스트가 정한 미팅 지속 시간에 맞지 않게 신청한 경우 예외 발생 시킨다.")
    @Test
    void applyAppointment2() {
        // given
        int meetingDuration = 30;
        Appointment appointment = Appointment.builder()
                .hostId(1L)
                .meetingDuration(meetingDuration)
                .meetingStartDate(LocalDate.of(2023, 8, 1))
                .meetingEndDate(LocalDate.of(2023, 8, 10))
                .title("test")
                .build();
        AvailableTime availableTime1 = AvailableTime.builder()
                .availableStartTime(LocalDateTime.of(2023, 8, 2, 18, 0))
                .availableEndTime(LocalDateTime.of(2023, 8, 2, 23, 0))
                .build();
        AvailableTime availableTime2 = AvailableTime.builder()
                .availableStartTime(LocalDateTime.of(2023, 8, 3, 18, 0))
                .availableEndTime(LocalDateTime.of(2023, 8, 3, 23, 0))
                .build();
        availableTime1.addAvailableTime(appointment);
        availableTime2.addAvailableTime(appointment);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        // when
        // then
        assertThatThrownBy(
                () -> applyService.applyAppointment(new ApplyCreateServiceRequest(1L, LocalDateTime.of(2023, 8, 2, 18, 0), LocalDateTime.of(2023, 8, 2, 19, 0), 1L)),
                "호스트가 정한 미팅 지속 시간은 30분인데 1시간 신청한 경우")
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorMessage.INVALID_MEETING_DURATION_TIME.getPhrase());
    }

    @DisplayName("사용자가 미팅 신청한 기간이 미팅 가능 시간이 아닌 경우 예외 발생 시킨다.")
    @Test
    void applyAppointment3() {
        // given
        int meetingDuration = 30;
        Appointment appointment = Appointment.builder()
                .hostId(1L)
                .meetingDuration(meetingDuration)
                .meetingStartDate(LocalDate.of(2023, 8, 1))
                .meetingEndDate(LocalDate.of(2023, 8, 10))
                .title("test")
                .build();
        AvailableTime availableTime1 = AvailableTime.builder()
                .availableStartTime(LocalDateTime.of(2023, 8, 2, 18, 0))
                .availableEndTime(LocalDateTime.of(2023, 8, 2, 23, 0))
                .build();
        AvailableTime availableTime2 = AvailableTime.builder()
                .availableStartTime(LocalDateTime.of(2023, 8, 3, 18, 0))
                .availableEndTime(LocalDateTime.of(2023, 8, 3, 23, 0))
                .build();
        availableTime1.addAvailableTime(appointment);
        availableTime2.addAvailableTime(appointment);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        // when
        // then
        assertThatThrownBy(
                () -> applyService.applyAppointment(new ApplyCreateServiceRequest(1L, LocalDateTime.of(2023, 8, 2, 17, 30), LocalDateTime.of(2023, 8, 2, 18, 0), 1L)),
                "미팅 신청 시간이 미팅 가능 기간 내에 포함되지 않는 경우")
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorMessage.INVALID_APPLY_TIME.getPhrase());
    }
}