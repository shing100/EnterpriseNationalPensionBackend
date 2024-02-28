package com.kingname.enterprisebackend.exception;

import lombok.Getter;

@Getter
public class NationPensionException extends RuntimeException {
    private ErrorCode errorCode;
    private String message;

    public NationPensionException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public NationPensionException(ErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
        this.message = detailMessage;
    }
}
