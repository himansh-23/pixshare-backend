package com.pixshare.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.pixshare.response.Response;

@ControllerAdvice
@Slf4j
public class GlobalExceptionController {
	
	@ExceptionHandler(UserException.class)
	public ResponseEntity<?> userExceptionHandler(UserException e)
	{
		log.error("User Error {}",e.toString());
		return new ResponseEntity<Response>(new Response(123, e.getMessage()),HttpStatus.OK);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> ExceptionHandler(UserException e)
	{
		log.error("User Error {}",e.toString());
		return new ResponseEntity<Response>(new Response(123, e.getMessage()),HttpStatus.OK);
	}

}
