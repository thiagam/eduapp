package org.nsna.controller;

import java.security.Principal;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.nsna.domain.CatagoryReportData;
import org.nsna.domain.ChangePasswdModal;
import org.nsna.domain.EduappConfig;
import org.nsna.domain.EduappConfigRepository;
import org.nsna.domain.EduappProcessDetail;
import org.nsna.domain.EduappProcessDetailRepository;
import org.nsna.domain.Eduapplication;
import org.nsna.domain.EduapplicationRepository;
import org.nsna.domain.ReviewerProgress;
import org.nsna.domain.User;
import org.nsna.domain.UserRepository;
import org.nsna.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EduProcessingController {
	private static final Logger logger = LoggerFactory.getLogger(EduProcessingController.class);
	
	@Autowired
	private EduapplicationRepository eduapplicationRepository;
	@Autowired
	private EduappProcessDetailRepository eduappProcesDetailRepository;
	@Autowired
	private EduappConfigRepository eduappConfigRepository;	
	@Autowired
	private UserRepository userRepository;
	@Autowired	
	private MailService mailService;	
	@Autowired
	private EntityManager em;

	// used in authentication process (security)
	@RequestMapping("/user")
	public User user(Principal principle) {
		User loginUser = userRepository.findByUserName(principle.getName());
		// clear the passwd before sending result to client.
		// (passwd should not be sent back to client)
		loginUser.setPasswordHash(null);
		return loginUser;
	}

	//return non ended, non hidden users
	@RequestMapping(value = "/getActiveUsers", method = RequestMethod.GET)
	public List<User> getActiveUsers() {
		List<User> results = userRepository.findActiveUsers();
		return results;
	}

	@RequestMapping(value = "/confirmChangePasswd", method = RequestMethod.POST)
	public boolean confirmChangePasswd(Principal principle, @RequestBody ChangePasswdModal changePasswdModel) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		User loginUser = userRepository.findByUserName(principle.getName());
		// if the provided current password match password in the db for the
		// user, then reset the password to new one provided.
		if (passwordEncoder.matches(changePasswdModel.getCurrentPasswd(), loginUser.getPasswordHash())) {
			// if
			// (loginUser.getPasswordHash().equals(changePasswdModel.getCurrentPasswd()))
			// {
			loginUser.setPasswordHash(passwordEncoder.encode(changePasswdModel.getNewPasswd()));
			userRepository.save(loginUser);
		} else {
			return false;
		}
		return true;
	}
	
    // Process form submission from forgotPassword page
	@RequestMapping(value = "/forgotPasswd", method = RequestMethod.POST)
	public boolean processForgotPasswordForm(@RequestParam("email") String userEmail, HttpServletRequest request) {

		// Lookup user in database by e-mail
		Optional<User> optional = userRepository.findByUserEmail(userEmail);

		if (!optional.isPresent() ) {
			return false;
			//modelAndView.addObject("errorMessage", "We didn't find an account for that e-mail address.");
		} else {
			
			User user = optional.get();
			
			if (!user.isAccountNonExpired()) {
				return false;
			} else {
			
				// Generate random 36-character string token for reset password 			
				user.setResetToken(UUID.randomUUID().toString());
	
				// Save token to database
				userRepository.save(user);
	
				String appUrl = request.getScheme() + "://" + request.getServerName();
				
				// Email message
				String emailMessage = "<p>We received a request to reset the account associated with this e-mail address. If you made this request, please follow the instructions below.</p>" 
						+"<p>&nbsp;</p>"
						+"<p>If you did not request to have your account reset, you can ignore this email.</p>"
						+"<p>&nbsp;&nbsp;&nbsp;</p>"
						+"<p>&nbsp;</p>"
						+"<p>Password Reset Instruction:</p>"
						+"<p>To reset your password, click the link below:</p>"
						+"<p>"
						+"<a href=\""+ appUrl + "/eduMain.html#!/reset/" + user.getResetToken() +"\">"
						+ appUrl + "/eduMain.html#!/reset/" + user.getResetToken() + "</a></p>";
				
				try {
					mailService.sendMail(userEmail, emailMessage, false);
				} catch(Exception ex){
					logger.error(ex.getMessage());
				}			
				
				// Add success message to view
				return true;
			}
		}
	}
	
	// Process reset password form
	@RequestMapping(value = "/resetPasswd", method = RequestMethod.POST)
	public boolean setNewPassword( @RequestParam("token") String token, @RequestParam("newPass") String newPasswd) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		// Find the user associated with the reset token
		Optional<User> optional = userRepository.findByResetToken(token);
		
		// This should always be non-null but we check just in case
		if (optional.isPresent()) {
			
			User resetUser = optional.get(); 
            
			// Set new password    
			resetUser.setPasswordHash(passwordEncoder.encode(newPasswd));
            
			// Set the reset token to null so it cannot be used again
			resetUser.setResetToken(null);

			// Save user
			userRepository.save(resetUser);

			return true;
			
		} else {
			return false;	
		}
		
   }
   	

	@RequestMapping(value = "/loadMyEduApplications", method = RequestMethod.GET)
	public List<Eduapplication> loadMyEduApplications(Principal principal) {
		// session.setAttribute("userName", principal.getName());
		String user = principal.getName();
		List<Eduapplication> results = eduapplicationRepository.findByEduappProcessDetailReviewer(user);
		return results;

	}
	


	@RequestMapping(value = "/loadEduApplication", method = RequestMethod.GET)
	public ArrayList loadEduApplication(@RequestParam("applId") long applID) {
		ArrayList results = new ArrayList();
		Eduapplication eduApp = eduapplicationRepository.findOne(applID);
		List<Eduapplication> potentialDup = eduapplicationRepository.findPossibleDuplicate(applID,
				eduApp.getStudentId(), eduApp.getApplicationYear(), eduApp.getBirthdate(), eduApp.getEmail(),
				eduApp.getFirstName(), eduApp.getLastName(), eduApp.getFathersName(), eduApp.getMothersName());
		List<Eduapplication> potentialOtherYearApps = eduapplicationRepository.findPossibleApplicantOtherYearApps(
				eduApp.getStudentId(), eduApp.getApplicationYear(), eduApp.getBirthdate(), eduApp.getEmail()
		// ,eduApp.getFirstName()
		);

		results.add(0, eduApp);
		results.add(1, potentialDup);
		results.add(2, potentialOtherYearApps);
		return results;
	}

	@RequestMapping(value = "/loadNewEduApplications", method = RequestMethod.GET)
	public List<Eduapplication> loadNewEduApplications() {
		List<Eduapplication> results = eduapplicationRepository.findByEduappProcessDetailProcessingStatus("New");
		return results;
	}

	@RequestMapping(value = "/memberSearchApplication", method = RequestMethod.POST)
	public List<Eduapplication> memberSearchApplication(@RequestParam("searchNameIdEmail") String searchNameIdEmail) {

		List<Eduapplication> results = null;
		if (!searchNameIdEmail.equals("")) {
			results = eduapplicationRepository.findByNameIdEmail("%" + searchNameIdEmail + "%", searchNameIdEmail);
		}
		return results;
	}

	@PreAuthorize("hasAuthority('admin')")
	@RequestMapping(value = "/searchEduApplication", method = RequestMethod.POST)
	public List<Eduapplication> searchEduApplication(@RequestParam("searchAppYear") String searchAppYear,
			@RequestParam("searchReviewer") String searchReviewer,
			@RequestParam("searchNameIdEmail") String searchNameIdEmail) {
		// List<Eduapplication> results =
		// eduapplicationRepository.findByFirstNameLike("Ab%");
		// List<Eduapplication> results =
		// eduapplicationRepository.findByFirstNameLike("Som%");

		List<Eduapplication> results = null;
		if (!searchNameIdEmail.equals("")) {
			results = eduapplicationRepository.findByNameIdEmail("%" + searchNameIdEmail + "%", searchNameIdEmail);
		} else if (!searchAppYear.equals("") && !searchReviewer.equals("")) {
			results = eduapplicationRepository.findByApplicationYearAndEduappProcessDetailReviewer(searchAppYear,
					searchReviewer);
		} else if (!searchAppYear.equals("")) {
			results = eduapplicationRepository.findByApplicationYear(searchAppYear);
		} else if (!searchReviewer.equals("")) {
			results = eduapplicationRepository.findByEduappProcessDetailReviewer(searchReviewer);
		}
		return results;
	}

	// Bulk update of processing details
	@RequestMapping(value = "/submitEduApplProcessDetails", method = RequestMethod.POST)
	public void submitEduApplProcessDetails(@RequestBody List<Eduapplication> eduApps) {
		eduapplicationRepository.save(eduApps);
	}

	@RequestMapping(value = "/submitEduApplProcessDetail", method = RequestMethod.POST)
	public void submitEduApplProcessDetail(@RequestBody EduappProcessDetail processDetail) {
		eduappProcesDetailRepository.save(processDetail);
	}

	private class StatusData {
		public String statusMsg;
	}

	@RequestMapping(value = "/public/checkApplicationStatus", method = RequestMethod.POST)
	public StatusData checkApplicationStatus(@RequestParam("confirmationNmbr") String confirmationNmbr,
			@RequestParam("studentId") String studentId, @RequestParam("birthDate") String birthDate) {

		List<EduappProcessDetail> appProcessingDetail = null;

		if (confirmationNmbr != null && confirmationNmbr.length() > 0) {
			appProcessingDetail = eduappProcesDetailRepository.findByApplicationNmbr(confirmationNmbr);
		} else {

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date dob = null;
			try {
				dob = formatter.parse(birthDate);
			} catch (Exception ex) {
			}
			;
			appProcessingDetail = eduappProcesDetailRepository.findByStudentIdAndBirthdate(studentId, dob);
		}
		
		List<EduappConfig> eduappConfigList = eduappConfigRepository.findAll();
		String  currentYear = eduappConfigList.get(0).getAppYear();
		String nextYear = (new Integer(Integer.parseInt(currentYear) + 1)).toString();

		StatusData statusData = new StatusData();
			//If the application exists for the current academic year
		if (appProcessingDetail.size() > 0 && 
				appProcessingDetail.get(0).getEduapplication().getApplicationYear().equals(currentYear)) {
			switch (appProcessingDetail.get(0).getProcessingStatus()) {
			case "New":
			case "Assigned":
			case "RecommendReject":
				statusData.statusMsg = "Application Received:  NSNA has successfully received your application for education scholarship. "
						+ "All completed applications will be reviewed by the Selection Committee from October through December. "
						+ "Status will be updated starting Oct 15th and updated periodically till Dec 31st. "
						+ "The final selection will be made by Jan 15th "
						+ "and Scholarship awards for candidates will be notified by Jan " + nextYear + ".";
				break;
			case "Rejected":
				statusData.statusMsg = "Thank you  for applying to the NSNA Education Assistance program. We are sorry to inform you that your application has not been selected for a scholarship at this time. We encourage you to apply again next year. All the best. Use nsna.edu@achi.org for all of your correspondence.";
				break;
			case "ReviewComplete":
				statusData.statusMsg = "Application Review InProgress: The application is being reviewed by the Selection Committee and the final selection list will be made by Jan 15th. Scholarship awards for candidates will be notified by Jan " + nextYear + ".";
				break;
			case "Approved":
				statusData.statusMsg = "Application Approved: Congratulations! Your application is approved for Scholarship.";
				break;
			case "Awarded":
				statusData.statusMsg = "Scholarship Awarded: Congratulations! Your scholarship award is being credited to your bank account. Please check your bank account and email us at nsna.edu@achi.org to confirm that you have received the money.";
				break;
			default:
				statusData.statusMsg = "";
				break;
			}
		} else {
			statusData.statusMsg = "No matching application found for this academic year. "
					+ "Email nsna.edu@achi.org for all of your correspondence.";
		}

		return statusData;
	}
	
	
//////////////////////////////////Report data request methods/////////////////////////////////
	
	@RequestMapping(value = "/getOverAllProgress", method = RequestMethod.GET)
	public List<ReviewerProgress> getOverAllProgress(@RequestParam(value="applicationYear", required = false) String applicationYear) {

		if (applicationYear == null || applicationYear.equals("")) {
			List<EduappConfig> eduappConfigList = eduappConfigRepository.findAll();
			applicationYear = eduappConfigList.get(0).getAppYear();			
		}

		Query q = em.createNativeQuery("select 'All' reviewer, sum(CASEwhen (isnull(review_complete, 'N') = 'N',1 , 0)) AS \"Assigned_Count\"," //Pending count
			+ "sum(CASEwhen (review_complete = 'Y',1 , 0))  AS \"Completed_Count\""
			 + " from EDUAPP_PROCESS_DETAIL "
			+ " inner join EDUAPPLICATION on EDUAPP_PROCESS_DETAIL.EDUAPP_ID = EDUAPPLICATION.ID "
			+ "where EDUAPPLICATION.APPLICATION_YEAR = " + applicationYear , ReviewerProgress.class);
		List<ReviewerProgress> results = q.getResultList();
		return results;

	}		
	
	@RequestMapping(value = "/getReviewerProgress", method = RequestMethod.GET)
	public List<ReviewerProgress> getReviewerProgress(@RequestParam(value="applicationYear", required = false) String applicationYear) {

		if (applicationYear == null || applicationYear.equals("")) {
			List<EduappConfig> eduappConfigList = eduappConfigRepository.findAll();
			applicationYear = eduappConfigList.get(0).getAppYear();			
		}
		Query q = em.createNativeQuery("select isNull(REVIEWER, '-UnSelected') reviewer, count(*) AS \"Assigned_Count\","
			+ " sum(CASEwhen (review_complete = 'Y',1 , 0))  AS \"Completed_Count\" "
			+ " from EDUAPP_PROCESS_DETAIL "
			+ "inner join EDUAPPLICATION on EDUAPP_PROCESS_DETAIL.EDUAPP_ID = EDUAPPLICATION.ID "
			+ "where EDUAPPLICATION.APPLICATION_YEAR = " + applicationYear 
			+ "group by REVIEWER "
			+ "order by REVIEWER", ReviewerProgress.class);
		List<ReviewerProgress> results = q.getResultList();
		return results;

	}
	
	@RequestMapping(value = "/getRptCityData", method = RequestMethod.GET)
	public List<CatagoryReportData> getRptCityData(@RequestParam(value="applicationYear", required = false) String applicationYear) {

		if (applicationYear == null || applicationYear.equals("")) {
			List<EduappConfig> eduappConfigList = eduappConfigRepository.findAll();
			applicationYear = eduappConfigList.get(0).getAppYear();			
		}
		
		Query q = em.createNativeQuery("select top 10 CITY \"catagory\", count(*) as \"count\" from EDUAPPLICATION "
				+ "where APPLICATION_YEAR = " + applicationYear
				+ "group by CITY "
				+ "order by 2 desc, 1", CatagoryReportData.class);
		List<CatagoryReportData> results = q.getResultList();
		return results;
	}	
	
	@RequestMapping(value = "/getRptNativeData", method = RequestMethod.GET)
	public List<CatagoryReportData> getRptNativeData(@RequestParam(value="applicationYear", required = false) String applicationYear) {

		if (applicationYear == null || applicationYear.equals("")) {
			List<EduappConfig> eduappConfigList = eduappConfigRepository.findAll();
			applicationYear = eduappConfigList.get(0).getAppYear();			
		}
		
		Query q = em.createNativeQuery("select top 10 substring(NATIVE_VILLAGE, instr(NATIVE_VILLAGE,'(')) \"catagory\" , count(*) as \"count\" from EDUAPPLICATION "
				+ "where APPLICATION_YEAR = " + applicationYear
				+ "group by NATIVE_VILLAGE "
				+ "order by 2 desc, 1", CatagoryReportData.class);
		List<CatagoryReportData> results = q.getResultList();
		return results;
	}		
		
	@RequestMapping(value = "/getRptInsCityData", method = RequestMethod.GET)
	public List<CatagoryReportData> getRptInsCityData(@RequestParam(value="applicationYear", required = false) String applicationYear) {
		
		if (applicationYear == null || applicationYear.equals("")) {
			List<EduappConfig> eduappConfigList = eduappConfigRepository.findAll();
			applicationYear = eduappConfigList.get(0).getAppYear();			
		}
		
		Query q = em.createNativeQuery("select top 10 isNull(INSTITUTION_CITY,'unKnown') \"catagory\", count(*) as \"count\" from EDUAPPLICATION "
				+ "where APPLICATION_YEAR = " + applicationYear
				+ "group by INSTITUTION_CITY  "
				+ "order by 2 desc, 1", CatagoryReportData.class);
		List<CatagoryReportData> results = q.getResultList();
		return results;
	}	
	
	@RequestMapping(value = "/getRptDegreeData", method = RequestMethod.GET)
	public List<CatagoryReportData> getRptDegreeData(@RequestParam(value="applicationYear", required = false) String applicationYear) {
		
		if (applicationYear == null || applicationYear.equals("")) {
			List<EduappConfig> eduappConfigList = eduappConfigRepository.findAll();
			applicationYear = eduappConfigList.get(0).getAppYear();			
		}
		
		Query q = em.createNativeQuery("select top 10 UPPER(replace(LTRIM(RTRIM(DEGREE)), '.')) \"catagory\" , count(*) as \"count\" from EDUAPPLICATION "
				+ "where APPLICATION_YEAR = " + applicationYear
				+ "group by UPPER(replace(LTRIM(RTRIM(DEGREE)), '.')) "
				+ "order by 2 desc, 1", CatagoryReportData.class);
		List<CatagoryReportData> results = q.getResultList();
		return results;
	}	
	

	

	
}
