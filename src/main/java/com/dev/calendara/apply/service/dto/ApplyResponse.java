package com.dev.calendara.apply.service.dto;

import com.dev.calendara.apply.domain.Apply;
import com.dev.calendara.apply.domain.enumeration.ApplyStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ApplyResponse(
        Long applyId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
        LocalDateTime applyStartTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
        LocalDateTime applyEndTime,
        Long guestId,
        ApplyStatus applyStatus
) {
    public static ApplyResponse of(Apply apply) {
        return new ApplyResponse(
                apply.getId(),
                apply.getApplyStartTime(),
                apply.getApplyEndTime(),
                apply.getMemberId(),
                apply.getApplyStatus()
        );
    }
}
