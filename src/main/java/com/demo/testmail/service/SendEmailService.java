package com.demo.testmail.service;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class SendEmailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String fromEmailId;
	
	private static final Logger logger = LoggerFactory.getLogger(SendEmailService.class);
	
	
	public void sendEmail(Map<String,String> requestBody)
	{
		String recipient =requestBody.get("recipient");
		String body = requestBody.get("body");
		String subject = requestBody.get("subject");
		logger.info("Sending mail to {} ",recipient);
		try 
		{
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(fromEmailId);
		simpleMailMessage.setTo(recipient);
		simpleMailMessage.setText(body);
		simpleMailMessage.setSubject(subject);
		
		javaMailSender.send(simpleMailMessage);
		logger.info("Message sent successfully ");
		}
		catch(Exception e)
		{
			logger.error("Error while sending email to {}: {}", recipient, e.getMessage());
		}
	}
	
	public void sendEmailWithAttachment(Map<String,String> requestBody) throws MessagingException
	{
		String recipient =requestBody.get("recipient");
		String body = requestBody.get("body");
		String subject = requestBody.get("subject");
		String attachmentPath = requestBody.get("attachmentPath");
		
        MimeMessage mimeMessage =javaMailSender.createMimeMessage();
        MimeMessageHelper helper= new MimeMessageHelper(mimeMessage,true);
        
        
		logger.info("Sending mail to {} ",recipient);
		
		
		helper.setFrom(fromEmailId);
		helper.setTo(recipient);
		helper.setText(body);
		helper.setSubject(subject);
		
		FileSystemResource file = new FileSystemResource(new File(attachmentPath));
        helper.addAttachment(file.getFilename(), file);
        
        javaMailSender.send(mimeMessage);
	}
	
	public void sendEmailWithInlineImage(Map<String,String> requestBody) throws MessagingException {
	    
		String recipient =requestBody.get("recipient");
		String body = requestBody.get("body");
		String subject = requestBody.get("subject");
		String imagePath = requestBody.get("imagePath");
		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

	    helper.setFrom("your-email@gmail.com");
	    helper.setTo(recipient);
	    helper.setSubject(subject);
	    helper.setText(body, true);

	    // Add inline image
	    FileSystemResource res = new FileSystemResource(new File(imagePath));
	    helper.addInline("identifier1234", res);

	    javaMailSender.send(mimeMessage);
	}
	
	public void sendEmailToMultipleRecipients(String[] recipients, String[] ccRecipients, String[] bccRecipients, String body, String subject) throws MessagingException {
	    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

	    helper.setFrom("abc@gmail.com");
	    helper.setTo(recipients);
	    if (ccRecipients != null) {
	        helper.setCc(ccRecipients);
	    }
	    if (bccRecipients != null) {
	        helper.setBcc(bccRecipients);
	    }
	    helper.setSubject(subject);
	    helper.setText(body);

	    javaMailSender.send(mimeMessage);
	}
	
    //@Scheduled(cron = "34 11 * * * *")  // Schedule task to run every day at 11:34 AM (34th min 11 am *(day of the month) * (month) every month 
	public void sendScheduledEmail() {
	    try {
	        sendEmail(fromEmailId, "Scheduled email body", "Scheduled Email");
	        logger.info("Scheduled email sent.");
	    } catch (Exception e) {
	        logger.error("Error while sending scheduled email: {}", e.getMessage());
	    }
	}
	
	
	public void sendEmail(String recipient, String body, String subject) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(fromEmailId);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(body, true);  // true indicates the body contains HTML

            javaMailSender.send(mimeMessage);
            logger.info("Email sent successfully to {}", recipient);
        } catch (MessagingException e) {
            logger.error("Error while sending email to {}: {}", recipient, e.getMessage());
        }

	}
	
	@Retryable(
	        value = {MessagingException.class},  
	        maxAttempts = 3,                    
	        backoff = @Backoff(delay = 2000)
	    )
	    public void sendEmailWithRetry(String recipient, String body, String subject) throws MessagingException {
	        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	        try {
	            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

	            helper.setFrom(fromEmailId);
	            helper.setTo(recipient);
	            helper.setSubject(subject);
	            helper.setText(body, true);  

	            javaMailSender.send(mimeMessage);
	            logger.info("Email sent successfully to {}", recipient);
	        } catch (MessagingException e) {
	            logger.error("Error while sending email to {}: {}", recipient, e.getMessage());
	            throw e;  // Re-throw the exception to trigger retry
	        }
	    }

}
