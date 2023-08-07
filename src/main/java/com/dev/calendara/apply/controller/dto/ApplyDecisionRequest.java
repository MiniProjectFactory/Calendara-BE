package com.dev.calendara.apply.controller.dto;

import com.dev.calendara.apply.domain.enumeration.ApplyStatus;

public record ApplyDecisionRequest(
        Long hostId,
        Long applyId,
        ApplyStatus applyStatus
) {
}
