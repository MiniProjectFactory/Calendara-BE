package com.dev.calendara.appointment.controller;

import com.dev.calendara.appointment.controller.dto.AppointmentCreateRequest;
import com.dev.calendara.appointment.service.AppointmentService;
import com.dev.calendara.appointment.service.dto.AppointmentCreateResponse;
import com.dev.calendara.appointment.service.dto.AppointmentForm;
import com.dev.calendara.common.response.dto.BaseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping("/appointment")
    public BaseResponseDto<AppointmentCreateResponse> createAppointment(@RequestBody AppointmentCreateRequest appointmentCreateRequest) {
        return BaseResponseDto.ok(appointmentService.createAppointment(appointmentCreateRequest));
    }

    @GetMapping("/appointment")
    public BaseResponseDto<AppointmentForm> getAppointment(@RequestParam Long appointmentId) {
        return BaseResponseDto.ok(appointmentService.getAppointment(appointmentId));
    }
}
