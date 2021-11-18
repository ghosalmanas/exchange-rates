package com.wholesale.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
	 @ExceptionHandler(NoCurrencyAvailableException.class)
	  public final ResponseEntity<ExceptionResponse> handleNotFoundException(NoCurrencyAvailableException ex, WebRequest request) {
	    ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),
	        request.getDescription(false));
	    return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.NOT_ACCEPTABLE);
	  }
}
