package com.dev.calendara.common.exception.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
public enum ErrorMessage {
    INVALID_AVAILABLE_TIME(HttpStatus.BAD_REQUEST, "미팅 가능한 시간대는 미팅 기간내에 포함되어야 합니다."),
    INVALID_APPOINTMENT_DATE_RANGE(HttpStatus.BAD_REQUEST, "회의 신청 기간을 잘못 설정했습니다."),
    INTERVAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "요청을 처리하는 과정에서 서버가 예상하지 못한 오류가 발생하였습니다.");

    private final int code;
    private final String phrase;

    ErrorMessage(HttpStatus status, String phrase) {
        this.code = status.value();
        this.phrase = phrase;
    }
}
