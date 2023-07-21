package com.dev.calendara.appointment.service.dto;

import com.dev.calendara.availabletimes.AvailableTime;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AvailableTimeResponse(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
        LocalDateTime availableStartTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
        LocalDateTime availableEndTime
) {
    public static AvailableTimeResponse of(AvailableTime availableTime) {
        return new AvailableTimeResponse(availableTime.getAvailableStartTime(), availableTime.getAvailableEndTime());
    }
}
