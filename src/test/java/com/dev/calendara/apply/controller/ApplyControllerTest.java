package com.dev.calendara.apply.controller;

import com.dev.calendara.apply.controller.dto.ApplyCreateRequest;
import com.dev.calendara.apply.service.ApplyService;
import com.dev.calendara.apply.service.dto.ApplyCreateServiceResponse;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}