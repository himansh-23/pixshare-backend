package com.pixshare.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pixshare.dto.UserLogin;
import com.pixshare.dto.UserRegister;
import com.pixshare.exception.UserException;
import com.pixshare.response.Response;
import com.pixshare.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user/api")
@Slf4j
public class UserController {

	@Autowired
	UserService userService;
	
	@GetMapping
	public ResponseEntity<?> etc()
	{
		Response response=new Response(200,"Welcome");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegister userRegister,BindingResult bindingResult,HttpServletRequest servletRequest) throws UserException
	{
		if(bindingResult.hasErrors())
		{
			log.error("Data Not matching eligiable criteria");
			throw new UserException(123,"Data Not Matching Eligiable Criteria");
		}
		userService.regiseter(userRegister,servletRequest);
		Response response=new Response(200,"User Successfully Regiseter");
		log.info("Response Send {}",response);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody UserLogin userLogin) throws UserException
	{
		userService.login(userLogin);
		Response response=new Response(200,"User Successfully Login");
		log.info("Response Send {}",response);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@GetMapping("/verify/{token}")
	public ResponseEntity<?> verifyEmail(@PathVariable String token) throws UserException
	{
		System.out.println(token);
		userService.verifyEmail(token);
		Response response=new Response(200,"Verify Successfully");
		log.info("Response Send {}",response);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@PostMapping("/forgotpassword")
	public ResponseEntity<?> forgotPassword(@RequestParam String email,HttpServletRequest servletRequest) throws UserException
	{
		Response response=userService.findEmail(email,servletRequest)?new Response(200,"Email Found"):new Response(123,"Email Not Found");
		log.info("Response Send {}",response);

		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@PostMapping("/resetpage/{token}")
	public ResponseEntity<?> resetPage(@PathVariable String token,HttpServletRequest servletRequest)throws UserException
	{
		userService.resetPage(token, servletRequest);	
		Response response=new Response(200,"Password Change Link Send TO Your Mail");
		log.info("Response Send {}",response);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PostMapping("/updatepassword/{token}")
	public ResponseEntity<?> passwordChange(@PathVariable String token,@RequestParam String password,HttpServletRequest servletRequest) throws UserException
	{	
		userService.updatePassword(token,password);
		Response response=new Response(200,"Password Update Successfully");
		log.info("Response Send {}",response);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
}
