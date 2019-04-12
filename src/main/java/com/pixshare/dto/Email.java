package com.pixshare.dto;

import java.io.Serializable;

import com.pixshare.entity.UserDetails;

import lombok.Data;

@Data
public class Email implements Serializable {
	
	 UserDetails userDetails;
	String emailSubject;
	String emailContent;
	String path;
	
	public Email(UserDetails userDetails,String emailSubject,String emailContent,String path)
	{
		this.userDetails=userDetails;
		this.emailSubject=emailSubject;
		this.emailContent=emailContent;
		this.path=path;
	}

}
