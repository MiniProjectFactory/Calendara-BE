package com.dev.calendara.appointment.controller;

import com.dev.calendara.appointment.Appointment;
import com.dev.calendara.appointment.controller.dto.AppointmentCreateRequest;
import com.dev.calendara.appointment.service.AppointmentService;
import com.dev.calendara.appointment.service.dto.AppointmentCreateResponse;
import com.dev.calendara.availabletimes.AvailableTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(AppointmentController.class)
@ActiveProfiles("test")
class AppointmentControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("호스트가 미팅신청 할 수 있도록 생성한다.")
    @Test
    void createAppointment() throws Exception {
        // Given
        Appointment appointment = new Appointment("test", 3L, 30, LocalDate.of(2023, 7, 22), LocalDate.of(2023, 7, 30));
        AvailableTime availableTime = new AvailableTime(LocalDateTime.of(2023, 7, 24, 10, 0), LocalDateTime.of(2023, 7, 24, 12, 0));
        availableTime.addAvailableTime(appointment);
        AppointmentCreateResponse createResponse = AppointmentCreateResponse.of(appointment);
        when(appointmentService.createAppointment(any())).thenReturn(createResponse);

        AppointmentCreateRequest createRequest = new AppointmentCreateRequest("test", 1L, 30, LocalDate.of(2023, 7, 21), LocalDate.of(2023, 7, 30), new ArrayList<>());

        // When Then
        mockMvc.perform(
                        post("/api/v1/appointment")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.data.appointmentId").value(createResponse.appointmentId()))
                .andExpect(jsonPath("$.data.title").value(createResponse.title()))
                .andExpect(jsonPath("$.data.meetingDuration").value(createResponse.meetingDuration()))
                .andExpect(jsonPath("$.data.meetingStartDate").value(createResponse.meetingStartDate().toString()))
                .andExpect(jsonPath("$.data.meetingEndDate").value(createResponse.meetingEndDate().toString()))
                .andExpect(jsonPath("$.data.availableTimeResponses[0].availableStartTime").value(createResponse.availableTimeResponses().get(0).availableStartTime().toString()))
                .andExpect(jsonPath("$.data.availableTimeResponses[0].availableEndTime").value(createResponse.availableTimeResponses().get(0).availableEndTime().toString()))
        ;
    }


}
