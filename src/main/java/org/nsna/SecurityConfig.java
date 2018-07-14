package org.nsna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			// .csrf().disable()
			.authorizeRequests()
			.antMatchers("/", "/eduMain.html", "/login", "/logout", "/forgotPasswd", "/resetPasswd", "/public/**", 
					"/image/**", "/js/**", "/css/**", "/templates/public/**").permitAll()
			// .antMatchers("/templates/committee/**").hasAuthority("admin")
			.anyRequest().authenticated() //.permitAll()
			// .and().exceptionHandling().accessDeniedPage("/403")
			.and().httpBasic().and().addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class).csrf()
			.csrfTokenRepository(csrfTokenRepository());
		 //.and().logout().logoutSuccessUrl("/").permitAll();



		http.sessionManagement()
				// .maximumSessions(1)
				// .expiredUrl("/eduMain.html")
				// .maxSessionsPreventsLogin(true)
				// .and()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
		// .invalidSessionUrl("/");
		
		http.
        exceptionHandling()
            .accessDeniedHandler(new CustomAccessDeniedHandler());
		 
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		//auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authProvider());
	}
	
    // Register HttpSessionEventPublisher
    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }	
    
    @Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}    
    
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }    
}
