package com.dev.calendara.apply.controller;

import com.dev.calendara.apply.controller.dto.ApplyCreateRequest;
import com.dev.calendara.apply.service.ApplyService;
import com.dev.calendara.apply.service.dto.ApplyCreateServiceResponse;
import com.dev.calendara.common.response.dto.BaseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ApplyController {

    private final ApplyService applyService;

    @PostMapping("/apply")
    public BaseResponseDto<ApplyCreateServiceResponse> applyAppointment(@RequestBody ApplyCreateRequest applyCreateRequest) {
        return BaseResponseDto.ok(applyService.applyAppointment(applyCreateRequest.of()));
    }
}
