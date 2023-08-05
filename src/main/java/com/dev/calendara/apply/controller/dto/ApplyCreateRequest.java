package com.dev.calendara.apply.controller.dto;

import com.dev.calendara.apply.service.dto.ApplyCreateServiceRequest;

import java.time.LocalDateTime;

public record ApplyCreateRequest(
        Long appointmentId,
        LocalDateTime meetingStartTime,
        LocalDateTime meetingEndTime,
        Long memberId
) {

    public ApplyCreateServiceRequest of() {
        return new ApplyCreateServiceRequest(appointmentId, meetingStartTime, meetingEndTime, memberId);
    }
}
