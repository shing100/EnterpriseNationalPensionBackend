package com.kingname.enterprisebackend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class NationalPensionProviderExceptionHandler {

    @ExceptionHandler(NationPensionException.class)
    public ErrorResponse descriptionExceptionHandler(NationPensionException e, HttpServletRequest request) {
        log.error("errorCode: {}, url: {}, message: {}", e.getErrorCode(), request.getRequestURI(), e.getMessage());
        return ErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class, MethodArgumentNotValidException.class})
    public ErrorResponse handleBadRequest(Exception e, HttpServletRequest request) {
        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage());
        return ErrorResponse.builder()
                .errorCode(ErrorCode.INTERNAL_SERVER_EXCEPTION)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e, HttpServletRequest request) {
        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage());
        return ErrorResponse.builder()
                .errorCode(ErrorCode.INTERNAL_SERVER_EXCEPTION)
                .message(e.getMessage())
                .build();
    }
}
