package com.dev.calendara.apply.controller;

import com.dev.calendara.apply.controller.dto.ApplyCreateRequest;
import com.dev.calendara.apply.controller.dto.ApplyDecisionRequest;
import com.dev.calendara.apply.controller.dto.ApplyListRequest;
import com.dev.calendara.apply.domain.enumeration.ApplyStatus;
import com.dev.calendara.apply.service.ApplyService;
import com.dev.calendara.apply.service.dto.ApplyCreateServiceResponse;
import com.dev.calendara.apply.service.dto.ApplyDecisionResponse;
import com.dev.calendara.apply.service.dto.AppointmentApplyListResponse;
import com.dev.calendara.member.dto.MemberResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class ApplyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApplyService applyService;

    @Test
    @DisplayName("게스트가 미팅 신청을 한다.")
    void applyAppointment() throws Exception {
        // Given
        ApplyCreateRequest applyCreateRequest = new ApplyCreateRequest(1L, LocalDateTime.of(2023, 8, 3, 12, 0), LocalDateTime.of(2023, 8, 3, 12, 30), 1L);
        ApplyCreateServiceResponse applyCreateServiceResponse = new ApplyCreateServiceResponse(1L);
        when(applyService.applyAppointment(applyCreateRequest.of())).thenReturn(applyCreateServiceResponse);

        // When Then
        mockMvc.perform(
                        post("/api/v1/apply")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(applyCreateRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.data.applyId").value(1))
        ;
    }

    @Test
    @DisplayName("호스트는 본인이 오픈한 미팅에 대해 신청한 미팅 신청 내역을 확인할 수 있다.")
    void getApplyAppointment() throws Exception {
        // Given
        AppointmentApplyListResponse response = new AppointmentApplyListResponse(1L,
                ApplyStatus.WAIT,
                LocalDateTime.of(2023, 8, 6, 18, 30),
                LocalDateTime.of(2023, 8, 6, 19, 0),
                "test",
                new MemberResponse("test", "test@test.com")
        );
        when(applyService.getAppointmentApplyList(new ApplyListRequest(1L, 1L))).thenReturn(List.of(response));

        // When Then
        mockMvc.perform(
                        get("/api/v1/apply/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("memberId", "1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.data[0].applyId").value(1))
                .andExpect(jsonPath("$.data[0].applyStatus").value(ApplyStatus.WAIT.toString()))
                .andExpect(jsonPath("$.data[0].applyStartTime").value("2023-08-06T18:30:00"))
                .andExpect(jsonPath("$.data[0].applyEndTime").value("2023-08-06T19:00:00"))
                .andExpect(jsonPath("$.data[0].title").value("test"))
                .andExpect(jsonPath("$.data[0].member.name").value("test"))
                .andExpect(jsonPath("$.data[0].member.email").value("test@test.com"))
        ;
    }

    @Test
    @DisplayName("게스트가 신청한 미팅을 호스트가 승인/반려처리 한다.")
    void decisionApply() throws Exception {
        // Given
        ApplyDecisionRequest applyDecisionRequest = new ApplyDecisionRequest(1L, 1L, ApplyStatus.APPROVE);
        ApplyDecisionResponse applyDecisionResponse = new ApplyDecisionResponse(1L, ApplyStatus.APPROVE);
        when(applyService.decisionApply(applyDecisionRequest)).thenReturn(applyDecisionResponse);

        // When Then
        mockMvc.perform(
                        patch("/api/v1/apply")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(applyDecisionRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.data.applyId").value(1))
                .andExpect(jsonPath("$.data.applyStatus").value(ApplyStatus.APPROVE.toString()))
        ;
    }
}