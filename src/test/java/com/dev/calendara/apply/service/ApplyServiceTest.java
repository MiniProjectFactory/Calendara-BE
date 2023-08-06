package com.dev.calendara.apply.service;

import com.dev.calendara.apply.controller.dto.ApplyDecisionRequest;
import com.dev.calendara.apply.controller.dto.ApplyListRequest;
import com.dev.calendara.apply.domain.Apply;
import com.dev.calendara.apply.domain.enumeration.ApplyStatus;
import com.dev.calendara.apply.service.dto.ApplyCreateServiceRequest;
import com.dev.calendara.apply.service.dto.ApplyCreateServiceResponse;
import com.dev.calendara.apply.service.dto.ApplyDecisionResponse;
import com.dev.calendara.apply.service.dto.AppointmentApplyListResponse;
import com.dev.calendara.appointment.Appointment;
import com.dev.calendara.appointment.repository.AppointmentRepository;
import com.dev.calendara.availabletimes.AvailableTime;
import com.dev.calendara.common.exception.custom.BusinessException;
import com.dev.calendara.common.exception.dto.ErrorMessage;
import com.dev.calendara.member.Member;
import com.dev.calendara.member.dto.MemberResponse;
import com.dev.calendara.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ApplyServiceTest {

    @Autowired
    private ApplyService applyService;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private MemberRepository memberRepository;

    @DisplayName("게스트가 미팅 정보를 작성 후 신청할 수 있다.")
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

    @DisplayName("게스트가 미팅 신청 시 호스트가 정한 미팅 지속 시간에 맞지 않게 신청한 경우 예외 발생 시킨다.")
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

    @DisplayName("게스트가 미팅 신청한 기간이 미팅 가능 시간이 아닌 경우 예외 발생 시킨다.")
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

    @Test
    @DisplayName("호스트가 오픈한 미팅에 대해서 신청한 내역을 가져온다.")
    void getAppointmentApplyList() {
        // given
        ApplyListRequest applyListRequest = new ApplyListRequest(1L, 1L);
        int meetingDuration = 30;
        Appointment appointment = Appointment.builder()
                .hostId(1L)
                .meetingDuration(meetingDuration)
                .meetingStartDate(LocalDate.of(2023, 8, 1))
                .meetingEndDate(LocalDate.of(2023, 8, 10))
                .title("test")
                .build();
        Apply apply = Apply.builder().appointment(appointment).applyStatus(ApplyStatus.WAIT).applyStartTime(LocalDateTime.of(2023, 8, 3, 19, 0)).applyEndTime(LocalDateTime.of(2023, 8, 3, 19, 30)).memberId(1L).build();
        Member member = new Member("test", "test@test.com");
        when(appointmentRepository.findByIdAndHostIdAndApplyStatus(any(Long.class), any(Long.class), eq(ApplyStatus.WAIT))).thenReturn(Optional.of(appointment));
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        // when
        List<AppointmentApplyListResponse> appointmentApplyList = applyService.getAppointmentApplyList(applyListRequest);

        // then
        assertThat(appointmentApplyList).hasSize(1)
                .extracting("applyStatus", "applyStartTime", "applyEndTime", "title", "member")
                .containsExactlyInAnyOrder(
                        tuple(apply.getApplyStatus(), apply.getApplyStartTime(), apply.getApplyEndTime(), appointment.getTitle(), new MemberResponse(member.getName(), member.getEmail()))
                );
    }

    @Test
    @DisplayName("게스트가 신청한 미팅을 호스트가 승인/반려처리 한다.")
    void decisionApply() {
        // given
        ApplyDecisionRequest applyDecisionRequest = new ApplyDecisionRequest(1L, 1L, ApplyStatus.APPROVE);
        Appointment appointment = Appointment.builder()
                .hostId(1L)
                .meetingDuration(30)
                .meetingStartDate(LocalDate.of(2023, 8, 1))
                .meetingEndDate(LocalDate.of(2023, 8, 10))
                .title("test")
                .build();
        Apply apply = Apply.builder().applyStatus(ApplyStatus.WAIT).applyStartTime(LocalDateTime.of(2023, 8, 3, 12, 0)).applyEndTime(LocalDateTime.of(2023, 8, 3, 12, 30)).appointment(appointment).build();
        ReflectionTestUtils.setField(apply, "id", 1L);
        when(appointmentRepository.findByHostIdAndApplyId(any(), any())).thenReturn(Optional.of(appointment));

        // when
        ApplyDecisionResponse applyDecisionResponse = applyService.decisionApply(applyDecisionRequest);

        // then
        assertThat(applyDecisionResponse.applyStatus()).isEqualTo(ApplyStatus.APPROVE);
    }
}