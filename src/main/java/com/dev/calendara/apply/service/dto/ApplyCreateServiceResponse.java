package com.dev.calendara.apply.service.dto;

import com.dev.calendara.apply.Apply;

public record ApplyCreateServiceResponse(
        Long applyId
) {
    public static ApplyCreateServiceResponse of(Apply savedApply) {
        return new ApplyCreateServiceResponse(savedApply.getId());
    }
}
