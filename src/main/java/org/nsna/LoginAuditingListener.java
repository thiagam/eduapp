package org.nsna;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.nsna.domain.EduapplicationRepository;
import org.nsna.domain.LoginAuditLog;
import org.nsna.domain.LoginAuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class LoginAuditingListener {
	
	@Autowired
	private LoginAuditLogRepository loginAuditLogRepository;

	//Log Login
	@EventListener
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		Authentication authentication = event.getAuthentication();

		WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
		String ip = details.getRemoteAddress();

		CurrentUser user = (CurrentUser) authentication.getPrincipal();
		String timeStamp = new SimpleDateFormat("yyyy MM dd HH:mm:ss").format(Calendar.getInstance().getTime());
		
		String message = "login: " + user.getUsername() + " : " + ip + " : " + timeStamp;		
		LoginAuditLog lal = new LoginAuditLog("login", user.getUsername(), ip, message);
		loginAuditLogRepository.save(lal);
//		System.out.println(message);

	}

	//Log Logout
	@EventListener
	public void onApplicationEvent(SessionDestroyedEvent event) {
		List<SecurityContext> lstSecurityContext = event.getSecurityContexts();
		CurrentUser user;
		for (SecurityContext securityContext : lstSecurityContext) {
			WebAuthenticationDetails details = (WebAuthenticationDetails) securityContext.getAuthentication()
					.getDetails();
			String ip = details.getRemoteAddress();

			user = (CurrentUser) securityContext.getAuthentication().getPrincipal();
			String timeStamp = new SimpleDateFormat("yyyy MM dd HH:mm:ss").format(Calendar.getInstance().getTime());
			
			String message = "logout: " + user.getUsername() + " : " + ip + " : " + timeStamp;		
			LoginAuditLog lal = new LoginAuditLog("logout", user.getUsername(), ip, message);
			loginAuditLogRepository.save(lal);			
//			System.out.println(message);

		}
	}

	//Log LoginFailure
	@EventListener
	public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
		Authentication authentication = event.getAuthentication();
		WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
		String ip = details.getRemoteAddress();

		String user = (String) authentication.getPrincipal();
		String timeStamp = new SimpleDateFormat("yyyy MM dd HH:mm:ss").format(Calendar.getInstance().getTime());
		
		String message = "loginFailure: " + user + " : " + ip + " : " + timeStamp;		
		LoginAuditLog lal = new LoginAuditLog("loginFailure", user, ip, message);
		loginAuditLogRepository.save(lal);			
//		System.out.println(message);		

	}

}
