package com.dev.calendara.common.response.dto;

import com.dev.calendara.common.exception.dto.ErrorMessage;

import static com.dev.calendara.common.response.dto.ResponseCode.INTERNAL_SERVER_ERROR;
import static com.dev.calendara.common.response.dto.ResponseCode.SUCCESS;

public record BaseResponseDto<T>(String code, String message, T data) {
    public static <T> BaseResponseDto<T> ok(T data) {
        return new BaseResponseDto<>(SUCCESS.getCode(), SUCCESS.getMessage(), data);
    }

    public static <T> BaseResponseDto<T> error(T data) {
        return new BaseResponseDto<>(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getMessage(), data);
    }

    public static <T extends ErrorMessage> BaseResponseDto<T> customError(T data) {
        return new BaseResponseDto<>(String.valueOf(data.getCode()), data.getPhrase(), data);
    }

    public static <T> BaseResponseDto<T> message(String code, String message, T data) {
        return new BaseResponseDto<>(code, message, data);
    }
}
