package com.kingname.enterprisebackend.exception;

import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor @Builder
public class ErrorResponse {
    private String message;
    private ErrorCode errorCode;
}
