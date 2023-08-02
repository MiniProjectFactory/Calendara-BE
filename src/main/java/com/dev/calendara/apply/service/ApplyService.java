package com.dev.calendara.apply.service;

import com.dev.calendara.apply.Apply;
import com.dev.calendara.apply.repository.ApplyRepository;
import com.dev.calendara.apply.service.dto.ApplyCreateServiceRequest;
import com.dev.calendara.apply.service.dto.ApplyCreateServiceResponse;
import com.dev.calendara.appointment.Appointment;
import com.dev.calendara.appointment.repository.AppointmentRepository;
import com.dev.calendara.availabletimes.AvailableTime;
import com.dev.calendara.common.exception.custom.BusinessException;
import com.dev.calendara.common.exception.dto.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplyService {

    private final ApplyRepository applyRepository;
    private final AppointmentRepository appointmentRepository;

    public ApplyCreateServiceResponse applyAppointment(ApplyCreateServiceRequest applyCreateServiceDto) {
        Appointment appointment = appointmentRepository.findById(applyCreateServiceDto.appointmentId()).orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_APPOINTMENT));
        LocalDateTime startTime = applyCreateServiceDto.meetingStartTime();
        LocalDateTime endTime = applyCreateServiceDto.meetingEndTime();
        
        verifyApplyTime(startTime, endTime, appointment);
        verifyAvailableTime(appointment, startTime, endTime);

        Apply apply = Apply.builder()
                .appointment(appointment)
                .memberId(applyCreateServiceDto.memberId())
                .applyEndTime(applyCreateServiceDto.meetingEndTime())
                .applyStartTime(applyCreateServiceDto.meetingStartTime())
                .confirmYn("N")
                .build();
        Apply savedApply = applyRepository.save(apply);

        return ApplyCreateServiceResponse.of(savedApply);
    }

    private void verifyAvailableTime(Appointment appointment, LocalDateTime startTime, LocalDateTime endTime) {
        List<AvailableTime> availableTimes = appointment.getAvailableTimes();
        boolean containAvailableTimeResult = availableTimes.stream().anyMatch(availableTime -> {
            LocalDateTime availableStartTime = availableTime.getAvailableStartTime();
            LocalDateTime availableEndTime = availableTime.getAvailableEndTime();
            return (startTime.equals(availableStartTime) || startTime.isAfter(availableStartTime)) &&
                    (endTime.equals(availableEndTime) || endTime.isBefore(availableEndTime));
        });

        if (!containAvailableTimeResult) {
            throw new BusinessException(ErrorMessage.INVALID_AVAILABLE_TIME);
        }
    }

    private void verifyApplyTime(LocalDateTime startTime, LocalDateTime endTime, Appointment appointment) {
        Duration duration = Duration.between(startTime, endTime);
        long applyMeetingDuration = duration.getSeconds();
        long appointmentMeetingDuration = Duration.ofMinutes(appointment.getMeetingDuration()).getSeconds();
        if (appointmentMeetingDuration != applyMeetingDuration) {
            throw new BusinessException(ErrorMessage.INVALID_MEETING_DURATION_TIME);
        }
    }
}
