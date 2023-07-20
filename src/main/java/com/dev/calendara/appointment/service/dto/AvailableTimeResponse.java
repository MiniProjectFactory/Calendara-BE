package com.dev.calendara.appointment.service.dto;

import com.dev.calendara.availabletimes.AvailableTime;

import java.time.LocalDateTime;

public record AvailableTimeResponse(
        LocalDateTime availableStartTime,
        LocalDateTime availableEndTime
) {
    public static AvailableTimeResponse of(AvailableTime availableTime) {
        return new AvailableTimeResponse(availableTime.getAvailableStartTime(), availableTime.getAvailableEndTime());
    }
}
