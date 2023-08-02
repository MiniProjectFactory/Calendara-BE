package com.dev.calendara.apply.service.dto;

import java.time.LocalDateTime;

public record ApplyCreateServiceRequest(
        Long appointmentId,
        LocalDateTime meetingStartTime,
        LocalDateTime meetingEndTime,
        Long memberId
) {
}
