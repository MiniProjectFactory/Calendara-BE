package com.dev.calendara.common.response.dto;

import static com.dev.calendara.common.response.dto.ResponseCode.INTERNAL_SERVER_ERROR;
import static com.dev.calendara.common.response.dto.ResponseCode.SUCCESS;

public record BaseResponseDto<T>(String code, String message, T data) {
    public static <T> BaseResponseDto<T> ok(T data) {
        return new BaseResponseDto<>(SUCCESS.getCode(), SUCCESS.getMessage(), data);
    }

    public static <T> BaseResponseDto<T> error(T data) {
        return new BaseResponseDto<>(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMessage(), data);
    }

    public static <T> BaseResponseDto<T> message(T data, String code, String message) {
        return new BaseResponseDto<>(code, message, data);
    }
}
