package org.nsna;


import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableAutoConfiguration
@ComponentScan
//@EnableOAuth2Sso
public class NSNAEduApplication {

    public static void main(String[] args) {
    	ApplicationContext ctx = SpringApplication.run(NSNAEduApplication.class, args);
  /*      
        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    */
    	}
  
    /*
    @Bean
	public CommonsMultipartResolver getMultipartResolver(){
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(8000000);
		return multipartResolver;
	};
	*/
    
    /*
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
     MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
     ObjectMapper objectMapper = new HibernateAwareObjectMapper();
     //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
     jsonConverter.setObjectMapper(objectMapper);
     return jsonConverter;
    }
    */
 }
