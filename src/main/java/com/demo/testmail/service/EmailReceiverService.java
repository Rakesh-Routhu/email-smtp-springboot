
package com.demo.testmail.service;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.search.FlagTerm;

@Service
public class EmailReceiverService {

    @Autowired
    private SendEmailService sendEmailService;

    @Value("${spring.mail.username}")
    private String username;

    //2 FA authentication through google account setting (16 digit passcode)
    @Value("${spring.mail.password}")
    private String password;

    private static final Logger logger = LoggerFactory.getLogger(EmailReceiverService.class);

    
    
    // IMAP has to be enabled in google account setting in order to check your new mails through this method 
    //@Scheduled(cron = "0 0/5 * * * ?") 
    public void checkForNewEmails() {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");

        try {
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false)); // Get unseen emails
            for (Message message : messages) {
                try {
                    String from = InternetAddress.toString(message.getFrom());
                    String subject = message.getSubject();

                    logger.info("New email received from: {}", from);
                    logger.info("Subject: {}", subject);

                    // Send automatic response
                    sendEmailService.sendEmail(from, "Thank you for your message", "Re: " + subject);

                    message.setFlag(Flags.Flag.SEEN, true); // Mark message as seen
                } catch (Exception e) {
                    logger.error("Error processing email: {}", e.getMessage());
                }
            }
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            logger.error("Error while checking for new emails: {}", e.getMessage());
        }
    }
}
