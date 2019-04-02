package com.pixshare.service;

import javax.servlet.http.HttpServletRequest;

import com.pixshare.dto.UserLogin;
import com.pixshare.dto.UserRegister;
import com.pixshare.exception.UserException;

public interface UserService {
	
	void regiseter(UserRegister userRegister,HttpServletRequest servletRequest) throws UserException;
	void login(UserLogin userLogin) throws UserException;
	void forgotPassword(String email) throws UserException;
	void verifyEmail(String token) throws UserException;
	boolean findEmail(String email,HttpServletRequest servletRequest) throws UserException;
	void resetPage(String token,HttpServletRequest servletRequest)throws UserException;
	void updatePassword(String token,String updatePassword)throws UserException;

}
