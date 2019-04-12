package com.pixshare;

import org.springframework.stereotype.Component;

import com.pixshare.dto.Email;
import com.pixshare.exception.UserException;
import com.pixshare.utils.EmailUtility;
import com.pixshare.utils.JwtToken;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessageReceiver {
	
	public void emailSend(Object msg)
	{
		Email email=(Email)msg;
		String token=JwtToken.generateToken(email.getUserDetails().getId());
		String emailContent=email.getEmailContent()+"\n\n"+email.getPath()+token;
		try {
			EmailUtility.sendEmail(email.getUserDetails().getEmail(), email.getEmailSubject(), emailContent);
			log.info("email send ");
		} catch (UserException e) {
			log.error("Unable To Send Email Due To {}",e);
		}
	}

}
