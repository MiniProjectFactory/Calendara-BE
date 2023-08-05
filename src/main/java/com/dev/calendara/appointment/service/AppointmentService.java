package com.dev.calendara.appointment.service;


import com.dev.calendara.appointment.Appointment;
import com.dev.calendara.appointment.controller.dto.AppointmentCreateRequest;
import com.dev.calendara.appointment.repository.AppointmentRepository;
import com.dev.calendara.appointment.service.dto.AppointmentCreateResponse;
import com.dev.calendara.appointment.service.dto.AppointmentForm;
import com.dev.calendara.appointment.service.dto.AvailableTimeDto;
import com.dev.calendara.availabletimes.AvailableTime;
import com.dev.calendara.common.exception.custom.BusinessException;
import com.dev.calendara.common.exception.dto.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentCreateResponse createAppointment(AppointmentCreateRequest appointmentCreateRequest) {
        Appointment appointment = toAppointment(appointmentCreateRequest);
        setAvailableTimesInAppointment(appointmentCreateRequest, appointment);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return AppointmentCreateResponse.of(savedAppointment);
    }

    private Appointment toAppointment(AppointmentCreateRequest appointmentCreateRequest) {
        return Appointment.builder()
                .title(appointmentCreateRequest.title())
                .hostId(appointmentCreateRequest.hostId())
                .meetingDuration(appointmentCreateRequest.meetingDuration())
                .meetingStartDate(appointmentCreateRequest.meetingStartDate())
                .meetingEndDate(appointmentCreateRequest.meetingEndDate())
                .build();
    }

    private void setAvailableTimesInAppointment(AppointmentCreateRequest appointmentCreateRequest, Appointment appointment) {
        appointmentCreateRequest.availableTimes().forEach(availableTimeDto -> {
            AvailableTime availableTime = AvailableTime.builder()
                    .availableStartTime(availableTimeDto.availableStartTime())
                    .availableEndTime(availableTimeDto.availableEndTime())
                    .build();
            availableTime.addAvailableTime(appointment);
        });
    }


    @Transactional(readOnly = true)
    public AppointmentForm getAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new BusinessException(ErrorMessage.NOT_FOUND_APPOINTMENT_FORM));
        List<AvailableTime> availableTimes = appointment.getAvailableTimes();
        return new AppointmentForm(
                appointment.getId(),
                appointment.getTitle(),
                appointment.getMeetingStartDate(),
                appointment.getMeetingEndDate(),
                appointment.getMeetingDuration(),
                availableTimes.stream().map(
                                availableTime ->
                                        new AvailableTimeDto(availableTime.getAvailableStartTime(),
                                                availableTime.getAvailableEndTime()))
                        .toList());
    }
}
