package com.dev.calendara.appointment.service.dto;

import com.dev.calendara.appointment.Appointment;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record AppointmentCreateResponse(
        Long appointmentId,
        String title,
        int meetingDuration,
        LocalDate meetingStartDate,
        LocalDate meetingEndDate,
        List<AvailableTimeResponse> availableTimeResponses
) {
    public static AppointmentCreateResponse of(Appointment appointment) {
        return AppointmentCreateResponse.builder()
                .appointmentId(appointment.getId())
                .title(appointment.getTitle())
                .meetingDuration(appointment.getMeetingDuration())
                .meetingStartDate(appointment.getMeetingStartDate())
                .meetingEndDate(appointment.getMeetingEndDate())
                .availableTimeResponses(
                        appointment.getAvailableTimes().stream()
                                .map(AvailableTimeResponse::of)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
