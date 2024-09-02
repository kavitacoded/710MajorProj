package com.nt.utils;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.nt.exception.MailSendException;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender mailSender;

	public boolean sendEmailMessage(String toMail,String subject,String body) throws MailSendException {
		boolean mailSentStatus=false;
		try {
			MimeMessage message=mailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(message);
			helper.setTo(toMail);
			helper.setSentDate(new Date());
			helper.setSubject(subject);
			helper.setText(body,true);
			mailSender.send(message);
			mailSentStatus=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mailSentStatus;
		
	}

}
