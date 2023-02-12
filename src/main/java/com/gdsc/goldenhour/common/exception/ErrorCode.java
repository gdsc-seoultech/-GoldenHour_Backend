package com.gdsc.goldenhour.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 403 FORBIDDEN 접근 실패
    MISSING_TOKEN(403, HttpStatus.FORBIDDEN, "토큰이 존재하지 않습니다."),
    INVALID_TOKEN(403, HttpStatus.FORBIDDEN, "토큰이 유효하지 않습니다."),
    INVALID_USER(403, HttpStatus.FORBIDDEN, "올바른 사용자가 아닙니다."),

    // 404 NOT_FOUND 존재하지 않음
    NOT_FOUND_ITEM(404, HttpStatus.NOT_FOUND, "항목이 존재하지 않습니다."),

    // 500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다.");

    private final int status;
    private final HttpStatus httpStatus;
    private final String message;
}
