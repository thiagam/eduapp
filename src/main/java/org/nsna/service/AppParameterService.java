package org.nsna.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppParameterService {
	
	@Value("${org.nsna.edu.storage.applAttachmentBackupRootFolder}")
	private  String applAttachmentBackupRootFolder;
	
	@Value("${org.nsna.edu.download.tempFolder}")
	private  String tempDownloadFolder;	
	
	public String getApplAttachmentBackupRootFolder(String region) {
		return applAttachmentBackupRootFolder + region +"\\";
	}
	
	public String getTempDownloadFolder(String region) {
		return tempDownloadFolder + region +"\\";
	}	

}
