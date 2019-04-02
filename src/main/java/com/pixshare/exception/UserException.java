package com.pixshare.exception;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class UserException extends Exception {

	private int sendErrorCode;
	private String sendErrorMsg;
	private List<String> sendErrorMsgList;
	public UserException(int sendErrorCode,String sendErrorMsg)
	{
		super(sendErrorMsg);
		this.sendErrorCode=sendErrorCode;
		this.sendErrorMsg=sendErrorMsg;
	}
	
	public UserException(int sendErrorCode,List<String> sendErrorMsg)
	{
		super();
	}
}
