package com.dev.calendara.appointment.service.dto;

import java.time.LocalDate;
import java.util.List;

public record AppointmentForm(
        Long appointmentId,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        int meetingDuration,
        List<AvailableTimeDto> availableTimes
) {
}
