package com.pixshare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.pixshare.response.Response;

@ControllerAdvice
public class GlobalExceptionController {
	
	@ExceptionHandler(UserException.class)
	public ResponseEntity<?> userExceptionHandler(UserException e)
	{
		return new ResponseEntity<Response>(new Response(123, e.getMessage()),HttpStatus.OK);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> ExceptionHandler(UserException e)
	{
		return new ResponseEntity<Response>(new Response(123, e.getMessage()),HttpStatus.OK);
	}

}
