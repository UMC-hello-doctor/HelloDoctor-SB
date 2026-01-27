package com.example.umc.global.error;

import ch.qos.logback.core.status.ErrorStatus;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {
    private final String code;
    private final String message;

    public GeneralException(ErrorCode status) {
        super(status.getMessage());
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public GeneralException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}