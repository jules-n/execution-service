package com.ynero.ss.execution.exception.handlers;

import com.ynero.ss.exceptions.handlers.entities.RestError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class MainExceptionHandler extends ResponseEntityExceptionHandler {

   // @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        var webRequest = ((ServletWebRequest) request).getRequest();
        var restError = new RestError(HttpStatus.BAD_REQUEST, "Wrong input data", webRequest.getRequestURI(), webRequest.getMethod());
        return buildResponseEntity(restError);
    }

    private ResponseEntity<Object> buildResponseEntity(RestError error) {
        return new ResponseEntity(error, error.getStatus());
    }
}
