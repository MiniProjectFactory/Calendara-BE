package com.dev.calendara.appointment.controller;

import com.dev.calendara.appointment.Appointment;
import com.dev.calendara.appointment.controller.dto.AppointmentCreateRequest;
import com.dev.calendara.appointment.controller.dto.AvailableTimeRequest;
import com.dev.calendara.appointment.repository.AppointmentRepository;
import com.dev.calendara.availabletimes.AvailableTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @DisplayName("호스트가 미팅신청 할 수 있도록 생성한다.")
    @Test
    void createAppointment() throws Exception {
        // Given
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(LocalDateTime.of(2023, 7, 22, 14, 0), LocalDateTime.of(2023, 7, 22, 22, 0));
        AppointmentCreateRequest createRequest = new AppointmentCreateRequest("test", 1L, 30, LocalDate.of(2023, 7, 21), LocalDate.of(2023, 7, 30), List.of(availableTimeRequest));

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
                .andExpect(jsonPath("$.data.appointmentId").value(1))
                .andExpect(jsonPath("$.data.title").value(createRequest.title()))
                .andExpect(jsonPath("$.data.meetingDuration").value(createRequest.meetingDuration()))
                .andExpect(jsonPath("$.data.meetingStartDate").value(createRequest.meetingStartDate().toString()))
                .andExpect(jsonPath("$.data.meetingEndDate").value(createRequest.meetingEndDate().toString()))
                .andExpect(jsonPath("$.data.availableTimeResponses[0].availableStartTime").value(availableTimeRequest.availableStartTime().toString()))
                .andExpect(jsonPath("$.data.availableTimeResponses[0].availableEndTime").value(availableTimeRequest.availableEndTime().toString()))
        ;
    }

    @DisplayName("게스트가 미팅신청에 대한 정보를 가져온다.")
    @Test
    void getAppointment() throws Exception {
        // Given
        Appointment appointment = new Appointment("test", 3L, 30, LocalDate.of(2023, 7, 22), LocalDate.of(2023, 7, 30));
        AvailableTime availableTime = new AvailableTime(LocalDateTime.of(2023, 7, 24, 10, 0), LocalDateTime.of(2023, 7, 24, 12, 0));
        availableTime.addAvailableTime(appointment);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // When Then
        mockMvc.perform(
                        get("/api/v1/appointment")
                                .param("appointmentId", savedAppointment.getId().toString())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.data.appointmentId").value(savedAppointment.getId()))
                .andExpect(jsonPath("$.data.title").value(savedAppointment.getTitle()))
                .andExpect(jsonPath("$.data.meetingDuration").value(savedAppointment.getMeetingDuration()))
                .andExpect(jsonPath("$.data.startDate").value(savedAppointment.getMeetingStartDate().toString()))
                .andExpect(jsonPath("$.data.endDate").value(savedAppointment.getMeetingEndDate().toString()))
                .andExpect(jsonPath("$.data.availableTimes[0].availableStartTime").value(availableTime.getAvailableStartTime().toString()))
                .andExpect(jsonPath("$.data.availableTimes[0].availableEndTime").value(availableTime.getAvailableEndTime().toString()))
        ;
    }

}
