package com.dev.calendara.appointment.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record AppointmentCreateRequest(
        String title,
        Long hostId,
        int meetingDuration,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate meetingStartDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate meetingEndDate,
        List<AvailableTimeRequest> availableTimes
) {
}
