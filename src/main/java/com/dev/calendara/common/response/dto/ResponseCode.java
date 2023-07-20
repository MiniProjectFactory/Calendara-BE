package com.dev.calendara.common.response.dto;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS("200", "SUCCESS"),
    INTERNAL_SERVER_ERROR("500", "INTERNAL_SERVER_ERROR");

    private String code;
    private String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
