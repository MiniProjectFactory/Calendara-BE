package com.dev.calendara.apply.service.dto;

import com.dev.calendara.apply.domain.enumeration.ApplyStatus;
import com.dev.calendara.member.dto.MemberResponse;

import java.time.LocalDateTime;

public record AppointmentApplyListResponse(Long applyId,
                                           ApplyStatus applyStatus,
                                           LocalDateTime applyStartTime,
                                           LocalDateTime applyEndTime,
                                           String title,
                                           MemberResponse memberId) {
}
