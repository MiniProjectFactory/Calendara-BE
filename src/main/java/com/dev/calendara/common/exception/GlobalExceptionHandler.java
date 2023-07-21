package com.dev.calendara.common.exception;

import com.dev.calendara.common.exception.custom.BusinessException;
import com.dev.calendara.common.exception.dto.ErrorMessage;
import com.dev.calendara.common.response.dto.BaseResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponseDto<ErrorMessage> businessExceptionHandle(BusinessException e) {
        log.warn("businessException : ", e);
        return BaseResponseDto.customError(e.getErrorMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponseDto<String> allUncaughtHandle(Exception e) {
        log.error("allUncaughtHandle : ", e);
        return BaseResponseDto.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

}
