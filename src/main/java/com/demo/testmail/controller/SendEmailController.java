package com.demo.testmail.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.testmail.service.SendEmailService;

import jakarta.mail.MessagingException;


@RestController
public class SendEmailController {
	
	
	@Autowired
	private SendEmailService sendEmailService;
	
	private static final Logger logger = LoggerFactory.getLogger(SendEmailController.class);
	
	@PostMapping("/sendEmail")
	public ResponseEntity<String> sendEmail(@RequestBody Map<String, String> requestBody) {
	    String recipient = requestBody.get("recipient");
	    String body = requestBody.get("body");
	    String subject = requestBody.get("subject");
	    
	    if (recipient == null || body == null || subject == null) {
	        return ResponseEntity.badRequest().body("Missing required fields: recipient, body, or subject.");
	    }
	    
	    try {
	        sendEmailService.sendEmail(requestBody);
	        return ResponseEntity.ok("Email processed successfully.");
	    } catch (Exception e) {
	        logger.error("Exception occurred while sending email: {}", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Failed to send email: " + e.getMessage());
	    }
	}
	
	
	@PostMapping("/sendEmailWithAttachment")
	public ResponseEntity<String> sendEmailWithAttachment(@RequestBody Map<String, String> requestBody) {
	    String recipient = requestBody.get("recipient");
	    String body = requestBody.get("body");
	    String subject = requestBody.get("subject");
	    String attachmentPath = requestBody.get("attachmentPath");

	    if (recipient == null || body == null || subject == null || attachmentPath == null) {
	        return ResponseEntity.badRequest().body("Missing required fields: recipient, body, or subject.");
	    }
	    
	    try {
	        sendEmailService.sendEmailWithAttachment(requestBody);
	        return ResponseEntity.ok("Email with attachment sent successfully.");
	    } catch (MessagingException e) {
	        logger.error("Error while sending email with attachment to {}: {}", recipient, e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("Failed to send email with attachment: " + e.getMessage());
	    }
	}
	
	@PostMapping("/sendEmailWithInlineImage")
	public ResponseEntity<String> sendEmailWithInlineImage(@RequestBody Map<String, String> requestBody) {
	    String recipient = requestBody.get("recipient");
	    String body = requestBody.get("body");
	    String subject = requestBody.get("subject");
	    String imagePath = requestBody.get("imagePath");

	    if (recipient == null || body == null || subject == null || imagePath == null) {
	        return ResponseEntity.badRequest().body("Missing required fields: recipient, body, or subject.");
	    }
	    
	    try {
	        sendEmailService.sendEmailWithInlineImage(requestBody);
	        return ResponseEntity.ok("Email with image sent successfully.");
	    } catch (MessagingException e) {
	        logger.error("Error while sending email with attachment to {}: {}", recipient, e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("Failed to send email with image: " + e.getMessage());
	    }
	}

}
