package org.nsna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppParameterService {
	
	private static final Logger logger = LoggerFactory.getLogger(AppParameterService.class);
	
	@Value("${org.nsna.edu.storage.applAttachmentBackupRootFolder}")
	private  String applAttachmentBackupRootFolder;
	
	@Value("${org.nsna.edu.download.tempFolder}")
	private  String tempDownloadFolder;	
	
	public String getApplAttachmentBackupRootFolder() {
		return applAttachmentBackupRootFolder;
	}
	
	public String getTempDownloadFolder() {
		return tempDownloadFolder;
	}	

}
