package com.dev.calendara.apply.service.dto;

import com.dev.calendara.apply.domain.enumeration.ApplyStatus;

public record ApplyDecisionResponse(
        Long applyId,
        ApplyStatus applyStatus
) {
}
