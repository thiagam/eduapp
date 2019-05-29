package org.nsna.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialException;

import org.nsna.domain.EduappAttachment;
import org.nsna.domain.EduappAttachmentData;
import org.nsna.domain.EduappConfig;
import org.nsna.domain.EduappConfigRepository;
import org.nsna.domain.EduappProcessDetail;
import org.nsna.domain.Eduapplication;
import org.nsna.domain.EduapplicationRepository;
import org.nsna.service.AppParameterService;
import org.nsna.service.MailService;
import org.nsna.service.ScholarshipOriginationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
public class EduApplicationController {

	private static final Logger logger = LoggerFactory.getLogger(EduApplicationController.class);

	@Autowired
	private EduapplicationRepository eduapplicationRepository;
	@Autowired
	private EduappConfigRepository eduappConfigRepository;
	
	@Autowired	
	private MailService mailService;
	
	@Autowired
	private AppParameterService appParameterService;

	@Autowired
	private ScholarshipOriginationService scholarshipOriginationService;
	
	@RequestMapping(value = "/public/getBlankEduApplication", method = RequestMethod.POST)
	public Eduapplication getBlankEduApplication(Principal principal, HttpServletRequest request,
			@RequestParam("oldStudent") String oldStudent,
			@RequestParam("studentId") String studentId,
			@RequestParam("birthDate") String birthDate) {
		Eduapplication app = new Eduapplication();
		if (oldStudent.equals("1")) { //Yes
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date dob = null;
			List<Eduapplication> results;

			try{ //StudentId and DOB
				dob = formatter.parse(birthDate);
			} catch (Exception ex) {};
			results = eduapplicationRepository.findByStudentIdAndBirthdateOrderByApplicationYearDesc(studentId, dob);
			
			if (results.size() > 0) {
				Eduapplication lastApp = results.get(0);
				app.setStudentId(lastApp.getStudentId());
				app.setBirthdate(lastApp.getBirthdate());
				app.setLastName(lastApp.getLastName());
				app.setFirstName(lastApp.getFirstName());
			}
		}
		// session.setAttribute("userName", principal.getName());
		System.out.println(request.getRemoteAddr());

	
		return app;
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/public/submitWithFile", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public void submitWithFile(Principal principal, HttpServletRequest request,
			@RequestPart(value = "photo",required = false) MultipartFile photo,			
			@RequestPart(value = "markSheet1") MultipartFile markSheet1,
			@RequestPart(value = "markSheet2", required = false) MultipartFile markSheet2,
			@RequestPart(value = "tuitionReceipt1") MultipartFile tuitionReceipt1,
			@RequestPart(value = "tuitionReceipt2", required = false) MultipartFile tuitionReceipt2,			
			@RequestPart(value = "incomeProof") MultipartFile incomeProof,
			@RequestPart(value = "nagaratharProof") MultipartFile nagaratharProof,
			@RequestPart(value = "scholarshipLetter") MultipartFile scholarshipLetter,
			@RequestPart(value = "application") String applicationStr, HttpSession session) throws IOException {
		
		submitApplication(principal, request, photo, markSheet1, markSheet2, tuitionReceipt1, tuitionReceipt2, 
				incomeProof, nagaratharProof, scholarshipLetter, applicationStr, session );
	}
	public void submitApplication(Principal principal, HttpServletRequest request,
			MultipartFile photo,
			MultipartFile markSheet1,
			MultipartFile markSheet2,
			MultipartFile tuitionReceipt1,
			MultipartFile tuitionReceipt2,			
			MultipartFile incomeProof,
			MultipartFile nagaratharProof,
			MultipartFile scholarshipLetter,
			String applicationStr, 
			HttpSession session) throws IOException {

		logger.debug("In submitWithFile method");
		Gson gson = new GsonBuilder()
				.setDateFormat("dd/MM/yyyy")
				.create();
		Eduapplication application = gson.fromJson(applicationStr, Eduapplication.class);
		
		String logInfo = application.getCrTs() + " : " +
				request.getRemoteHost() + " : " +
				application.getFirstName() + " " +application.getLastName() + " : " +
				application.getEmail() + " : " +				
				application.getPhone1();
		
		logger.info(logInfo);
		//System.out.println(logInfo);

		logger.debug("--json to object success");
		
		//set application Year -- get the application year from the config record.
		//String currentYear = new SimpleDateFormat("yyyy").format(new Date());
		List<EduappConfig> eduappConfigList = eduappConfigRepository.findByRegion(
				scholarshipOriginationService.getScholarshipOriginationRegion());
		String  currentYear = eduappConfigList.get(0).getAppYear();
		application.setApplicationYear(currentYear);
		logger.info("currentYear.."+currentYear);
		logger.info("Scholarship Award Notif Data:.."+eduappConfigList.get(0).getScholarshipAwardNotificationDate());
		logger.info("eduappConfigList toString:.."+ eduappConfigList.get(0).toString());
		
		// generate StudentId if one not passed.
		Random rand = new Random();		
		if (application.getStudentId() == null || application.getStudentId().equals("")) {
			
			application.setStudentId(application.getApplicationYear() + "-" + (rand.nextInt(89998) + 10001));
		}
		// generate 12char random confirmation nmbr
		application.setConfirmationNmbr(randomString(12));

		logger.debug("--student id, confirmation nmbr generation successful");
		
		if (photo != null) {
			addAttachment(application, photo, "photo");
		}
		
		addAttachment(application, markSheet1, "markSheet1");
		
		if (markSheet2 != null) {
			addAttachment(application, markSheet2, "markSheet2");
		}
		addAttachment(application, tuitionReceipt1, "tuitionReceipt1");
		if (tuitionReceipt2 != null) {
			addAttachment(application, tuitionReceipt2, "tuitionReceipt2");
		}
		addAttachment(application, incomeProof, "incomeProof");
		addAttachment(application, nagaratharProof, "nagaratharProof");
		addAttachment(application, scholarshipLetter, "scholarshipLetter");

		
		EduappProcessDetail appProcessDetail = new EduappProcessDetail();
		appProcessDetail.setProcessingStatus("New");
		appProcessDetail.setpBankName(application.getBankName());
		appProcessDetail.setpBeneficiaryAccountNumber(application.getAccountNumber());
		appProcessDetail.setpBeneficiaryAddressLine1(application.getAddressLine1());
		appProcessDetail.setpBeneficiaryAddressLine2(application.getAddressLine2());
		appProcessDetail.setpBeneficiaryAddressLine3(application.getCity().substring(0,Math.min(28, application.getCity().length()) ) 
							+ " " + application.getPin());
		appProcessDetail.setpBeneficiaryName(application.getCheckToName());
		appProcessDetail.setpBranchIfscCode(application.getBranchIfscCode());
		if (application.getBankSwiftCode() != null && application.getBankSwiftCode() != "" ) {
			appProcessDetail.setpBankSwiftCode(application.getBankSwiftCode());
			appProcessDetail.setUseSwift('Y');
		} else {
			appProcessDetail.setUseSwift('N');
		}
		appProcessDetail.setEduapplication(application);
		application.setEduappProcessDetail(appProcessDetail);

		application.setRemoteAddress(request.getRemoteHost());
		if (principal != null) {
			application.setUserName(principal.getName());
		}
		
		application.setRegion(scholarshipOriginationService.getScholarshipOriginationRegion());
		eduapplicationRepository.save(application);
		
		//backup all attachments in the backup file folder
		System.out.println(application.getId());
		backupAttachmentFiles(application);
		
		//set session values for confirmation page
		session.setAttribute("studentId", application.getStudentId());
		session.setAttribute("confirmationNmbr", application.getConfirmationNmbr());

		logger.debug("--Application created successful");

		String htmlMessage = "<body>" + "<style>" + "   table, th, td {" + "   border: 1px solid black;"
				+ "   border-collapse: collapse;" + " </style>"

				+ "<p >Dear Applicant,</p> " + "<p >&nbsp; &nbsp;&nbsp;</p>"
				+ "<p >&nbsp; &nbsp; &nbsp;"+ scholarshipOriginationService.getScholarshipOriginationLabel() +"has received your application for education scholarship. "
				+ "All applications will be reviewed and scholarship awarded for candidates will be notified by"
				+ eduappConfigList.get(0).getScholarshipAwardNotificationDate() + ". &nbsp;</p> "
				+ "<p >&nbsp; &nbsp;&nbsp;</p> "
				+ "<p >&nbsp; &nbsp; &nbsp; Please note your StudentID and application confirmation Number. It is important for you to provide these for any future communication.&nbsp;</p>"
				+ "				<table>" + "					<thead> " + "                    <th>Student ID:</th>"
				+ "                    <th>Application Confirmation Number:</th>" + "                </thead> "
				+ "                <tbody> " + "                    <tr> " + "                        <td>"
				+ application.getStudentId() + "</td> " + "                        <td >"
				+ application.getConfirmationNmbr() + "</td> " + "                    </tr> "
				+ "                </tbody>" + "            </table>" 
				+ " <br />"
				+ " <p >&nbsp; &nbsp;</p>"
				+ " <p><strong>NOTE: Do not reply to this email. For all future communication use "+ scholarshipOriginationService.getEmail() +"  </strong> </p>"
				+ " <p>Sincerely, </br> "+ scholarshipOriginationService.getScholarshipOriginationLabel() +" Education Committee </br>"
				+ " Email: "+ scholarshipOriginationService.getEmail() +" </br>"
				+ " Web Site: "+ scholarshipOriginationService.getWebsite() +" </p>"
				+ "</body>";

		try {
			mailService.sendMail(application.getEmail(), htmlMessage, true);
		} catch(Exception ex){
			logger.error(ex.getMessage());
		}
		logger.debug("--Mail generation successful. End of submitWithFile method");		
/*
 		//Send SMS through Twilio Service
		try {
			TwilioMessagingService.sendSMSMessage("1" + application.getPhone1(),  scholarshipOriginationService.getScholarshipOriginationLabel() +" "StudentId:"
				+ application.getStudentId() + "     Appl Confirmation Number: " + application.getConfirmationNmbr());
		} catch(Exception ex) {
			logger.error(ex.getMessage());
		}
*/	
		
/*		
		//Send SMS through Nexmo Service
		String smsMessage = scholarshipOriginationService.getScholarshipOriginationLabel() +""StudentId:" + application.getStudentId() + 
				"     Appl Confirmation Number: " + application.getConfirmationNmbr();
		nexmoMessagingService.sendSMSMessage("1" + application.getPhone1(), smsMessage);
*/
	}

	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static SecureRandom rnd = new SecureRandom();

	private String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	private void addAttachment(Eduapplication application, MultipartFile file, String documentCategory)
			throws IOException {
		byte[] bytes;
		bytes = file.getBytes();
		EduappAttachment eduappAttachment = new EduappAttachment();
		eduappAttachment.setDocumentOriginalFilename(file.getOriginalFilename());
		logger.debug("--processing attachment: " + file.getOriginalFilename());
		eduappAttachment.setDocumentCategory(documentCategory);
		eduappAttachment.setDocumentOrder(1);
		eduappAttachment.setEduapplication(application);
		application.getEduappAttachments().add(eduappAttachment);		

		
 //Code to save attachment file to database
//Don't comment this. The save of file to database is prevented by making the property in 
// Entity object as transient.
		EduappAttachmentData eduappAttachmentData = new EduappAttachmentData();

		try {
			eduappAttachmentData.setDocument(new javax.sql.rowset.serial.SerialBlob(bytes));
			eduappAttachmentData.setEduappAttachment(eduappAttachment);
			eduappAttachment.setEduappAttachmentData(eduappAttachmentData);
		} catch (SerialException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
//////////////////////////////////////////// */
		
/*		
		String applAttachmentBackupRootFolder = appParameterService.getApplAttachmentBackupRootFolder();
		//Create folder (if not exists) to store application attachments
		File theDir = new File(applAttachmentBackupRootFolder + application.getId() + "_" +application.getStudentId());
		//File theDir = new File("C:\\Users\\Somasundaram\\Tech-Work\\EduAppFiles\\" + application.getStudentId());
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			logger.debug("creating directory: " + theDir.getName());
		    try{
		        theDir.mkdir();
		    } 
		    catch(SecurityException se){
		    	logger.error(se.getMessage());
		    }        
		}
		
		//Store attachment
		String path = theDir + "\\" + documentCategory + "-" + file.getOriginalFilename();
		FileOutputStream stream = new FileOutputStream(path);
		try {
		    stream.write(bytes);
		} finally {
		    stream.close();
		}
*/
	}
	
	
	private void backupAttachmentFiles (Eduapplication application) throws IOException {
		String applAttachmentBackupRootFolder = appParameterService.getApplAttachmentBackupRootFolder(
 				scholarshipOriginationService.getScholarshipOriginationRegion());
		//Create folder (if not exists) to store application attachments
		File theDir = new File(applAttachmentBackupRootFolder + application.getId() + "_" +application.getStudentId());
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			logger.debug("creating directory: " + theDir.getName());
		    try{
		        theDir.mkdir();
		    } 
		    catch(SecurityException se){
		    	logger.error(se.getMessage());
		    }        
		}
		
		Iterator<EduappAttachment> i = application.getEduappAttachments().iterator();
		while (i.hasNext()) {
			EduappAttachment eduappAttachment = i.next();
			
			//Store attachment
			String path = theDir + "\\" + eduappAttachment.getDocumentCategory() + "-" + eduappAttachment.getDocumentOriginalFilename();
			FileOutputStream stream = null;
			Blob attachmentFile;
			try {
				stream = new FileOutputStream(path);

				attachmentFile = eduappAttachment.getEduappAttachmentData().getDocument();

				if ((int)attachmentFile.length() > 0){
					stream.write(attachmentFile.getBytes(1,(int)attachmentFile.length()));
				}
			}catch (SQLException ex) {
				logger.error(ex.getMessage());				
			}finally {
			    stream.close();
			}			
		}
	}
	

	private class ConfirmationData {
		public String studentId;
		public String confirmationNmbr;
	}

	@RequestMapping(value = "/public/getConfirmationData", method = RequestMethod.GET)
	public ConfirmationData getConfirmationData(HttpSession session) {
		ConfirmationData confirmationData = new ConfirmationData();
		confirmationData.studentId = (String) session.getAttribute("studentId");
		confirmationData.confirmationNmbr = (String) session.getAttribute("confirmationNmbr");
		session.removeAttribute("studentId");
		session.removeAttribute("confirmationNmbr");
		return confirmationData;

	}
	
	@RequestMapping(value = "/public/getEduAppConfig", method = RequestMethod.GET)
	public EduappConfig getEduAppConfig() {
		List<EduappConfig> results = eduappConfigRepository.findByRegion(
				scholarshipOriginationService.getScholarshipOriginationRegion());
		return results.get(0);
	}		

	private class UpdateSwiftCodeData {
		public String confirmationNmbr;
		public String applicantName;
		public String bankName;
		public String branchName;
		public String swiftCode;
		public int recordFound;
		
		public String statusMsg;
	}	
	
	//This method is to support collecting SWIFT code for 2016 applications.
	@RequestMapping(value = "/public/updateSwiftCodeStep1", method = RequestMethod.POST)
	public UpdateSwiftCodeData updateSwiftCodeStep1(
			@RequestParam("confirmationNmbr") String confirmationNmbr,
			@RequestParam("studentId") String studentId, 
			@RequestParam("birthDate") String birthDate) {
		
		List<Eduapplication> eduApplications = null;
		UpdateSwiftCodeData updateSwiftCodeData = new UpdateSwiftCodeData();

		if (confirmationNmbr != null && confirmationNmbr.length() > 0) {
			eduApplications = eduapplicationRepository.findByConfirmationNmbr(confirmationNmbr);
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date dob = null;
			try {
				dob = formatter.parse(birthDate);
			} catch (Exception ex) {
			}
			eduApplications = eduapplicationRepository.findByStudentIdAndBirthdateOrderByApplicationYearDesc(studentId, dob);
		}
		if (eduApplications.size()>0 && eduApplications.get(0).getApplicationYear().equals("2016")) {
			Eduapplication eduApplication = eduApplications.get(0);
			updateSwiftCodeData.statusMsg =  "Hello " + eduApplication.getFirstName() + " " + 
					eduApplication.getLastName() + ", Please enter the Swift Code for your Bank branch: " +
					eduApplication.getBankName() + "-" + eduApplication.getBranchName();
			
			updateSwiftCodeData.recordFound = 1;
			updateSwiftCodeData.applicantName = eduApplication.getFirstName() + " " + 
					eduApplication.getLastName();
			updateSwiftCodeData.bankName = eduApplication.getBankName();
			updateSwiftCodeData.branchName = eduApplication.getBranchName();
			updateSwiftCodeData.confirmationNmbr = eduApplication.getConfirmationNmbr();
			if ((eduApplication.getBankSwiftCode() != null) && !eduApplication.getBankSwiftCode().equals("")) {
				updateSwiftCodeData.swiftCode = eduApplication.getBankSwiftCode();
				updateSwiftCodeData.recordFound = 2;
			}
		} else {
			updateSwiftCodeData.recordFound = 0;
			updateSwiftCodeData.statusMsg = "No matching application found. Use back button to reenter the input. Use "
					+ scholarshipOriginationService.getEmail() + " for all of your correspondence.";
		}
		
		return updateSwiftCodeData;
		
	}

	@RequestMapping(value = "/public/updateSwiftCodeStep2", method = RequestMethod.POST)
	public void updateSwiftCodeStep2(
			@RequestParam("confirmationNmbr") String confirmationNmbr,
			@RequestParam("swiftCode") String swiftCode) {
		int updateCount = eduapplicationRepository.updateSwiftCode(swiftCode, confirmationNmbr);
	}
	

}
