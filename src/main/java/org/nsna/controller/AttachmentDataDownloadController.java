package org.nsna.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;
import org.nsna.domain.EduappAttachment;
import org.nsna.domain.EduappAttachmentRepository;
import org.nsna.service.AppParameterService;
import org.nsna.service.ScholarshipOriginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AttachmentDataDownloadController {
	
	@Autowired
	private EduappAttachmentRepository eduapppAttachmentRepository;
	
	@Autowired
	private AppParameterService appParameterService;	
	
	@Autowired
	private ScholarshipOriginationService scholarshipOriginationService;
	
	@RequestMapping(value="/downloadAttachment", method = RequestMethod.GET)
    public void downloadFile(@RequestParam("attachmentId") long attachmentId, HttpServletResponse response) throws IOException, SQLException {
		
		EduappAttachment attachment = eduapppAttachmentRepository.findOne(attachmentId);
		
		String fileName = attachment.getDocumentOriginalFilename();
		String extension = fileName.substring(fileName.lastIndexOf("."));
		//System.out.println(extension);
/*		
		String mimeType= URLConnection.guessContentTypeFromName(attachment.getDocumentOriginalFilename());
        if (mimeType == null) {
        	if (extension.equals(".docx")) {
        		mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        	} else if (extension.equals(".doc")) {
        		mimeType = "application/msword";
        	} else if (extension.equals(".xls")) {
        		mimeType = "application/vnd.ms-excel";
        	} else if (extension.equals(".xlsx")) {
        		mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        	}
        }
        //System.out.println("mimetype : "+mimeType);
        response.setContentType(mimeType);	

        // "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
        //while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + attachment.getDocumentOriginalFilename() +"\""));

        Blob attachmentFile = attachment.getEduappAttachmentData().getDocument();
        response.setContentLength((int)attachmentFile.length());
        
        InputStream inputStream = new BufferedInputStream(attachmentFile.getBinaryStream()); 
*/		
        
        File file = null;
 		String applAttachmentBackupRootFolder = appParameterService.getApplAttachmentBackupRootFolder(
 				scholarshipOriginationService.getScholarshipOriginationRegion());
 		String theDir = applAttachmentBackupRootFolder + attachment.getEduapplication().getId() + "_" + attachment.getEduapplication().getStudentId();

 		String path = theDir + "\\" + attachment.getDocumentCategory() + "-" + attachment.getDocumentOriginalFilename(); 		
        //System.out.println(path);
        file = new File(path);
        
         
        if(!file.exists()){
            String errorMessage = "Sorry. The file you are looking for does not exist";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;
        }
        
		String mimeType= URLConnection.guessContentTypeFromName(attachment.getDocumentOriginalFilename());
        if (mimeType == null) {
        	if (extension.equals(".docx")) {
        		mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        	} else if (extension.equals(".doc")) {
        		mimeType = "application/msword";
        	} else if (extension.equals(".xls")) {
        		mimeType = "application/vnd.ms-excel";
        	} else if (extension.equals(".xlsx")) {
        		mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        	}
        }
        //System.out.println("mimetype : "+mimeType);
        response.setContentType(mimeType);	
        
        
        // // "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
        // //    while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
 
         
        // // "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting
        //response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
         
        response.setContentLength((int)file.length());
 
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        
        //Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

}
