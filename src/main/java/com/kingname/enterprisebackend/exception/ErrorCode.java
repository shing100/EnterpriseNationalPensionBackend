package com.kingname.enterprisebackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum ErrorCode {
    INTERNAL_SERVER_EXCEPTION("서버에 오류가 발생했습니다."),
    EXIST_INDEX_EXCEPTION("이미 있는 인덱스 입니다."),
    NOT_EXIST_INDEX_EXCEPTION("존재하지 않는 인덱스 입니다.")
    ;
    private final String message;
}
