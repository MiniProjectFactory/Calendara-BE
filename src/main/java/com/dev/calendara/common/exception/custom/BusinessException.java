package com.dev.calendara.common.exception.custom;

import com.dev.calendara.common.exception.dto.ErrorMessage;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorMessage errorMessage;

    public BusinessException(ErrorMessage errorMessage) {
        super(errorMessage.getPhrase());
        this.errorMessage = errorMessage;
    }
}
