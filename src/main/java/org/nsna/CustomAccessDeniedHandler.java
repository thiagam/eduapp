package org.nsna;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		if (accessDeniedException instanceof MissingCsrfTokenException) {
			response.setStatus(440);
			/*
			 * Handle as a session timeout (redirect, etc). Even better if you
			 * inject the InvalidSessionStrategy used by your
			 * SessionManagementFilter, like this:
			 * invalidSessionStrategy.onInvalidSessionDetected(request,
			 * response);
			 */
		} else {
			response.setStatus(403);
			/* Redirect to a error page, send HTTP 403, etc. */
		}

	}
}
