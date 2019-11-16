package com.pixshare.utils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import com.pixshare.exception.UserException;

import lombok.extern.slf4j.Slf4j;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Slf4j
public class EmailUtility {
	
	public static void sendEmail(String toEmail,String subject,String body) throws UserException{
		
		Properties pros=new Properties();
		pros.put("mail.smtp.auth", true);
		pros.put("mail.smtp.starttls.enable", true);
		pros.put("mail.smtp.host","smtp.gmail.com");
		pros.put("mail.smtp.port", "587");
		
		Authenticator auth=new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("dummy12hi@gmail.com","Dummy@12");
			}
		};
		
		toEmail="prajapat.himanshu@gmail.com";
		Session session=Session.getInstance(pros, auth);
		Message msg = new MimeMessage(session);
		try {
		 msg.setFrom(new InternetAddress("no_reply@gmail.com", "NoReply-JD"));
	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
	      msg.setSubject(subject);
	      msg.setContent(body, "text/html");
    	  Transport.send(msg);  
    	  
		}
		catch(UnsupportedEncodingException | MessagingException e)
		{
			log.error("Email Exception",e);
			throw new UserException(123, e.getMessage());
		} 
	}

}
