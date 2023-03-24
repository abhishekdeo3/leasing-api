package com.car.leasing.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;

import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(responseCode = "400", content = {
            @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class))
    })
    @ExceptionHandler({ConstraintViolationException.class, MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class, MissingRequestHeaderException.class, BadRequestException.class})
    public Problem exceptionHandler(Exception e) {

        log.warn("BAD_REQUEST exception handled in RestResponseEntityExceptionHandler: {}", e.getMessage(), e);
        return new Problem(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(responseCode = "400", content = {
            @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class))
    })
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, DataIntegrityViolationException.class})
    public Problem methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e) {

        log.warn("BAD_REQUEST exception handled in ApiExceptionHandler: {}", e.getMessage(), e);
        return new Problem(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Invalid value was specified for parameter: " + e.getName());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ApiResponse(responseCode = "403", content = {
            @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class))
    })
    @ExceptionHandler(AccessDeniedException.class)
    public Problem accessDeniedExceptionHandler(AccessDeniedException e) {

        log.warn("FORBIDDEN exception handled in ApiExceptionHandler: {}", e.getMessage(), e);
        return new Problem(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                "Not enough permissions to perform the requested action");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(responseCode = "404", content = {
            @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class))
    })
    @ExceptionHandler({NotFoundException.class})
    public Problem exceptionHandler(RuntimeException e) {

        log.warn("NOT_FOUND exception handled in RestResponseEntityExceptionHandler: {}", e.getMessage(), e);
        return new Problem(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponse(content = {
            @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class))
    })
    @ExceptionHandler(Exception.class)
    public Problem handleException(Exception e) {

        log.error("Exception handled in RestResponseEntityExceptionHandler: {}", e.getMessage(), e);
        return new Problem(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                e.getCause().toString());
    }

    @Getter
    @Setter
    private static class Problem {

        private @NotNull int status;
        private String title;
        private String detail;

        public Problem(int status, String title, String detail) {
            this.status = status;
            this.title = title;
            this.detail = detail;
        }
    }
}