package com.pixshare.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pixshare.AppConfiguration;
import com.pixshare.dao.UserRepository;
import com.pixshare.dto.Email;
import com.pixshare.dto.UserLogin;
import com.pixshare.dto.UserRegister;
import com.pixshare.entity.UserDetails;
import com.pixshare.exception.UserException;
import com.pixshare.utils.EmailUtility;
import com.pixshare.utils.JwtToken;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Override
	public void regiseter(UserRegister userRegister,HttpServletRequest servletRequest) throws UserException {
		UserDetails userDetails=modelMapper.map(userRegister, UserDetails.class);
		userDetails.setPassword(passwordEncoder.encode(userRegister.getPassword()));
		 userDetails=userRepository.save(userDetails);
		 String requestBy=servletRequest.getRequestURL().toString();
		 requestBy=requestBy.substring(0,requestBy.length()-8)+"verify/";
		 rabbitTemplate.convertAndSend(AppConfiguration.exchangeForEmail, "", new Email(userDetails,"Email Verification Link","please Verify Your Email",requestBy));
		log.info("email send");
	}

	@Override
	public void login(UserLogin userLogin) throws UserException {

		Optional<UserDetails> userDetails=userRepository.findByEmail(userLogin.getEmail());

		if(userDetails.isPresent()){
			if(!userDetails.get().isAuthenticated())
				{
					throw new UserException(123, "User Not Auth.");
				}
			boolean checkPassword=passwordEncoder.matches(userLogin.getPassword(), userDetails.get().getPassword());

			if(!checkPassword){
				throw new UserException(123, "Invalid Password");
			}

		}
		else {
			throw new UserException(123,"user Not Found");
		}
//		userRepository.findByEmail(userLogin.getEmail())
//						.map( userDetails -> {
//							if(userDetails.isAuthenticated()){
//
//							}
//							else {
//								throw new UserException(123, "User Not Auth.");
//							} throws UserException;
//						}).orElseThrow(() -> new UserException(123,"user Not Found"));
				}
	
//	private void validUser(UserDetails userDetails,String password) throws UserException
//	{
//		if(!userDetails.isAuthenticated())
//		{
//			throw new UserException(123, "User Not Auth.");
//		}
//		System.out.println(password +"   "+ userDetails.getPassword());
//		boolean b=passwordEncoder.matches(password, userDetails.getPassword());
//		if(!b){
//			throw new UserException(123, "Invalid Password");}
//	}

	@Override
	public void forgotPassword(String email) throws UserException {
		
		userRepository.findByEmail(email).map(userDetails ->  this.sendEmail(userDetails)).orElseThrow(() -> new UserException(123, "Email Send Unsuccessfully"));
	}
	
	private Optional<Boolean> sendEmail(UserDetails userDetails) 
	{
		try {
			
			EmailUtility.sendEmail(userDetails.getEmail(), "ForgotPassword", "Click On Below Link To ResetPassword");
			} catch (UserException e) {
			return Optional.empty();
			}
		return Optional.of(true);
	}

	@Override
	public void verifyEmail(String token) throws UserException {
		
		long id=JwtToken.verifyToken(token);
		UserDetails userDetails=userRepository.findById(id).get();
		userDetails.setAuthenticated(true);
		userRepository.save(userDetails);
	}

	@Override
	public boolean findEmail(String email,HttpServletRequest servletRequest) throws UserException{
		
		Optional<UserDetails> userDetails=userRepository.findByEmail(email);
		if(userDetails.isPresent())  {
			String requestBy=servletRequest.getRequestURL().toString();
			 requestBy=requestBy.substring(0,requestBy.length()-14)+"resetpage/";
			 rabbitTemplate.convertAndSend(AppConfiguration.exchangeForEmail, "", new Email(userDetails.get(),"Password Recovery","Click On below link",requestBy));
			 return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void resetPage(String token, HttpServletRequest servletRequest) throws UserException {
		Long id=JwtToken.verifyToken(token);
		UserDetails userDetails=userRepository.findById(id).get();
		String requestBy=getUrl(servletRequest);
		requestBy=requestBy+"/updatepassword/";
		rabbitTemplate.convertAndSend(AppConfiguration.exchangeForEmail, "", new Email(userDetails,"Reset Page","Click On below link",requestBy));
	}

	@Override
	public void updatePassword(String token, String updatePassword) throws UserException {

		Long id=JwtToken.verifyToken(token);
		UserDetails user=userRepository.findById(id).get();
		user.setPassword(passwordEncoder.encode(updatePassword));
		userRepository.save(user);
	}
	
	private String getUrl(HttpServletRequest servletRequest)
	{
		StringBuffer url=servletRequest.getRequestURL();
		System.out.println(url);
		int count=2;
		while(count>0)
		{
			if(url.charAt(url.length()-1)=='/')
			{
				count--;
			}
			url.deleteCharAt(url.length()-1);
			
		}
		System.out.println(url.toString());
		return url.toString();
	}

	
}
