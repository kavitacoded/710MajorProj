package com.nt.util;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import io.micrometer.observation.transport.SenderContext;
import jakarta.mail.internet.MimeMessage;
@Component
public class EmailUtils {

	@Autowired
	private static JavaMailSender sender;
	
	public static void  sendMail(String email,String subject,String body,File file) throws Exception {
		boolean mailSentStatus=false;
			MimeMessage message=sender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(message);
			helper.setTo(email);
			helper.setSentDate(new Date());
			helper.setSubject(subject);
			helper.setText(body,true);
			helper.addAttachment(file.getName(), file);
			sender.send(message);
			mailSentStatus=true;
	}

}
