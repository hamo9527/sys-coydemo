package com.cailu.bom.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final int httpStatus;

    public BusinessException(String message) {
        this(message, 400);
    }

    public BusinessException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
