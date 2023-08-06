package com.dev.calendara.apply.controller;

import com.dev.calendara.apply.controller.dto.ApplyCreateRequest;
import com.dev.calendara.apply.controller.dto.ApplyDecisionRequest;
import com.dev.calendara.apply.controller.dto.ApplyListRequest;
import com.dev.calendara.apply.service.ApplyService;
import com.dev.calendara.apply.service.dto.ApplyCreateServiceResponse;
import com.dev.calendara.apply.service.dto.ApplyDecisionResponse;
import com.dev.calendara.apply.service.dto.AppointmentApplyListResponse;
import com.dev.calendara.common.response.dto.BaseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ApplyController {

    private final ApplyService applyService;

    @PostMapping("/apply")
    public BaseResponseDto<ApplyCreateServiceResponse> applyAppointment(@RequestBody ApplyCreateRequest applyCreateRequest) {
        return BaseResponseDto.ok(applyService.applyAppointment(applyCreateRequest.of()));
    }

    @GetMapping("/apply/{appointmentId}")
    public BaseResponseDto<List<AppointmentApplyListResponse>> getAppointmentApplyList(@PathVariable Long appointmentId, @RequestParam Long memberId) {
        return BaseResponseDto.ok(applyService.getAppointmentApplyList(new ApplyListRequest(appointmentId, memberId)));
    }

    @PatchMapping("/apply")
    public BaseResponseDto<ApplyDecisionResponse> decisionApply(@RequestBody ApplyDecisionRequest applyDecisionRequest) {
        return BaseResponseDto.ok(applyService.decisionApply(applyDecisionRequest));
    }
}
