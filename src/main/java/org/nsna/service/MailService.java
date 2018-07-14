package org.nsna.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.nsna.controller.EduApplicationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class MailService {
	
	private static final Logger logger = LoggerFactory.getLogger(MailService.class);
	
	@Value("${org.nsna.mail.smtp.host}")
	private  String host;	
	

	@Value("${org.nsna.mail.smtp.port}")
	private  String port;
	
	@Value("${org.nsna.mail.smtp.auth}")
	private  boolean auth;	
	
	@Value("${org.nsna.mail.smtp.starttls.enable}")
	private  boolean starttls;	
	
	@Value("${org.nsna.mail.username}")
	private  String username; 
	
	@Value("${org.nsna.mail.password}")	
	private  String password;
	
	@Value("${org.nsna.mail.ccEmailId}")	
	private  String ccEmailId;	

	public  void sendMail(String toEmail, String emailMessage, boolean cc ) {
		// Assuming you are sending email through relay.jangosmtp.net
		//String host = "smtp.gmail.com"; //email-smtp.us-west-2.amazonaws.com		
		
		//final String username = "somasundaramsp21@gmail.com";// change accordingly
		//final String password = "aishas21";// change accordingly

		// Sender's email ID needs to be mentioned
		String from = "noreply@achi.org";// change accordingly

		Properties props = new Properties();
		/*
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");
		*/
		
		props.put("mail.smtp.auth", auth);
		props.put("mail.smtp.starttls.enable", starttls);
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		
		// Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			if (cc) {
				message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmailId));
			}

			// Set Subject: header field
			message.setSubject("NSNA Education Application Confirmation");

			// Now set the actual text message
			//message.setText(emailMessage );
			
			message.setContent(emailMessage, "text/html");

			// Send message
			Transport.send(message);

			logger.debug("Mail Sent successfully....");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public  String getHost() {
		return host;
	}

	public  void setHost(String host) {
		host = host;
	}

	public  String getPort() {
		return port;
	}

	public  void setPort(String port) {
		port = port;
	}

	public  boolean isAuth() {
		return auth;
	}

	public  void setAuth(boolean auth) {
		auth = auth;
	}

	public  boolean isStarttls() {
		return starttls;
	}

	public  void setStarttls(boolean starttls) {
		starttls = starttls;
	}

	public  String getUsername() {
		return username;
	}

	public  void setUsername(String username) {
		username = username;
	}

	public  String getPassword() {
		return password;
	}

	public  void setPassword(String password) {
		password = password;
	}


	

}