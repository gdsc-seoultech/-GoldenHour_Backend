package com.gdsc.goldenhour.common.exception;

import com.gdsc.goldenhour.common.GoogleIdTokenProvider;
import com.gdsc.goldenhour.common.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.logging.Logger;

// ToDo: Error log 남기기 (Logger ? slf4f?)
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = Logger.getLogger(GoogleIdTokenProvider.class.getName());

    @ExceptionHandler(CustomCommonException.class)
    public ResponseEntity<ResponseDto<Object>> customCommonException(CustomCommonException e) {
        return new ResponseEntity<>(ResponseDto.fail(e.getStatus(), e.getHttpStatus(), e.getMessage()), e.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Object>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        errorList.forEach(
            objectError -> sb.append(objectError.getDefaultMessage())
        );

        return new ResponseEntity<>(ResponseDto.fail(400, HttpStatus.BAD_REQUEST, sb.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseDto<Object>> missingServletRequestParameterException(MissingServletRequestParameterException e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getParameterType());
        sb.append(" : ");
        sb.append(e.getParameterName());
        sb.append(" 파라미터가 존재하지 않습니다.");

        return new ResponseEntity<>(ResponseDto.fail(400, HttpStatus.BAD_REQUEST, sb.toString()), HttpStatus.BAD_REQUEST);
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
