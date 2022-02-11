package com.emulator.atmservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {


    @ExceptionHandler(value = CardNotValidException.class)
    public ResponseEntity<ErrorResponse> cardNotValidExceptionHandle(CardNotValidException exception) {
        log.error("Invalid Card {}", exception.getMessage(), exception);
        return buildErrorResponse(
                ErrorCode.CARD_NOT_VALID,
                exception.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserNotExistsException.class)
    public ResponseEntity<ErrorResponse> userNotExistsException(UserNotExistsException exception) {
        log.error("User not found {}", exception.getMessage(), exception);

        return buildErrorResponse(
                ErrorCode.USER_NOT_FOUND,
                exception.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> invalidCredentialsExceptionException(InvalidCredentialsException exception) {
        log.error("Invalid credentials {}", exception.getMessage(), exception);

        return buildErrorResponse(
                ErrorCode.INVALID_CREDENTIALS,
                exception.getMessage(),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = RuntimeException.class)
    public void runTimeExceptionHandle(RuntimeException exception) {
        log.error("Runtime exception {}", exception.getMessage(), exception);
    }

    @ExceptionHandler(value = FeignException.class)
    public ResponseEntity<ErrorResponse> feignExceptionHandle(FeignException exception, HttpServletResponse response) {
        log.error("Third party call has failed {}", exception.getMessage(), exception);

        return buildWithResponseBodyElseDefaultError(exception, response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("Invalid argument for the method: {}",exception.getMessage(),exception);

        return buildErrorResponse(
                ErrorCode.INVALID_ARGUMENT,
                exception.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(ErrorCode errorCode, String message, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .body(ErrorResponse.builder()
                        .errorCode(errorCode)
                        .message(message)
                        .build());
    }


    private ResponseEntity<ErrorResponse> buildWithResponseBodyElseDefaultError(
            FeignException exception,
            HttpServletResponse response) {
        ObjectMapper mapper = new ObjectMapper();

        ErrorResponse errorResponse;
        try {
            log.info("Trying to parse feign response body{}", exception.contentUTF8());
            errorResponse = mapper.readValue(exception.contentUTF8(), ErrorResponse.class);
        } catch (JsonProcessingException e) {
            log.warn("failed to parse feign response body, returning default response {}", exception.contentUTF8());

            return buildErrorResponse(
                    ErrorCode.UNEXPECTED_ERROR,
                    exception.getMessage(),
                    HttpStatus.valueOf(response.getStatus()));
        }
        return buildErrorResponse(
                errorResponse.getErrorCode(),
                errorResponse.getMessage(),
                HttpStatus.valueOf(response.getStatus()));
    }
}
