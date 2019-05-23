package org.nsna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ScholarshipOriginationService {
	
	private static final Logger logger = LoggerFactory.getLogger(ScholarshipOriginationService.class);
	
	@Value("${region}")
	private  String scholarshipOriginationRegion;	

	@Value("${label}")
	private  String scholarshipOriginationLabel;	

	@Value("${portalUsedFromYear}")
	private  String portalUsedFromYear;	

	@Value("${email}")
	private  String email;	

	@Value("${noReplyEmail}")
	private  String noReplyEmail;	

	@Value("${website}")
	private  String website;	

	public String getScholarshipOriginationRegion() {
		return scholarshipOriginationRegion;
	}	

	public String getScholarshipOriginationLabel() {
		return scholarshipOriginationLabel;
	}	

	public String getPortalUsedFromYear() {
		return portalUsedFromYear;
	}	
	
	public String getEmail() {
		return email;
	}	

	public String getNoReplyEmail() {
		return noReplyEmail;
	}	

	public String getWebsite() {
		return website;
	}	
}
