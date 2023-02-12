package com.gdsc.goldenhour.common.exception;

import com.gdsc.goldenhour.common.GoogleIdTokenProvider;
import com.gdsc.goldenhour.common.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Logger;

// ToDo: Error log 남기기 (Logger ? slf4f?)
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = Logger.getLogger(GoogleIdTokenProvider.class.getName());

    @ExceptionHandler(CustomCommonException.class)
    public ResponseEntity<ResponseDto<Object>> customCommonException(CustomCommonException e) {
        return new ResponseEntity<>(ResponseDto.fail(e.getStatus(), e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Object>> exception(Exception e) {
        log.warning(e.getMessage());
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(ResponseDto.fail(
                errorCode.getStatus(), errorCode.getHttpStatus(), errorCode.getMessage()
        ), errorCode.getHttpStatus());
    }
}
