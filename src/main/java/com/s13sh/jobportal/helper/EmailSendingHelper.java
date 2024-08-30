package com.s13sh.jobportal.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.s13sh.jobportal.dto.PortalUser;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSendingHelper {
	
	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	TemplateEngine templateEngine;
	
	public void sendOtp(PortalUser portalUser) {
		MimeMessage message=mailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		try {
		helper.setFrom("saishkulkarni7@gmail.com","Job-Portal");
		helper.setTo(portalUser.getEmail());
		helper.setSubject("Otp Verification Process");
		
		Context context=new Context();
		context.setVariable("name", portalUser.getName());
		context.setVariable("otp", portalUser.getOtp());
		
		String text=templateEngine.process("MyEmail.html", context);
		
		helper.setText(text,true);
		
		mailSender.send(message);
		System.out.println("Otp is Sent to Email " + portalUser.getEmail());
		}
		catch (Exception e) {
			System.out.println("Error - Not Able to Send Email");
		}
	}
}
