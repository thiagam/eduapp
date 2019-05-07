package org.nsna.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.h2.store.fs.FileUtils;
import org.h2.tools.Csv;
import org.nsna.domain.EduappConfig;
import org.nsna.domain.EduappConfigRepository;
import org.nsna.domain.User;
import org.nsna.domain.UserRepository;
import org.nsna.service.AppParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReportsController {
	/*
	@Autowired
	private EntityManager em;	
	*/
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private AppParameterService appParameterService;	
	@Autowired
	private UserRepository userRepository;	
	@Autowired
	private EduappConfigRepository eduappConfigRepository;		
	

	@RequestMapping(value="/downloadDataAsCSV", method = RequestMethod.GET)
    public void downloadDataCSV(HttpServletResponse response) throws IOException, SQLException {
		
		//Connection conn = em.unwrap(Connection.class)
		Connection conn = dataSource.getConnection();;
	    Statement stat = conn.createStatement();
	    
	    stat.execute("CALL CSVWRITE('" + "C:/Users/Somasundaram/gitrepo/NSNAEduApp/NSNAEduApp" + "/test.csv', 'SELECT * FROM EDUAPPLICATION')");
        conn.close();
        
	    String mimeType= "application/vnd.ms-excel";

        response.setContentType(mimeType);		
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + "data.csv" +"\""));
        InputStream inputStream = FileUtils.newInputStream("C:/Users/Somasundaram/gitrepo/NSNAEduApp/NSNAEduApp/test.csv");
        response.setContentLength((int)FileUtils.size("C:/Users/Somasundaram/gitrepo/NSNAEduApp/NSNAEduApp/test.csv"));
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }	
	
	@PreAuthorize("hasAuthority('admin')")
	@RequestMapping(value="/downloadAppWithReviewerData", method = RequestMethod.GET)
    public void downloadAppWithReviewerData(Principal principle,
    		HttpServletResponse response, String passcode, String passwd,
    		int reportYear) throws IOException, SQLException {
		
		if (isAuthorizedForReports(principle, passcode, passwd )) {
			downloadDataAsCSV(response, 
					"SELECT * FROM EDUAPPLICATION e " +
					" INNER JOIN EDUAPP_PROCESS_DETAIL ep on e.ID = ep.EDUAPP_ID " +
					" WHERE APPLICATION_YEAR = " + reportYear,
					"AppWithReviewerData-"+reportYear);
		} else {
			downloadDataAsCSV(response,"SELECT ''","blank");
		}
	}
	
	


/*	--testing code for http.post option
	@RequestMapping(value="/downloadAwardMailingAddressData", method = RequestMethod.POST)	
	public void downloadAwardMailingAddressData(Principal principle,
	    		HttpServletResponse response, 
	    		@RequestBody String aaa
	    		//String passcode, String passwd, String awdref
	    		) throws IOException, SQLException {
		
		System.out.println(aaa);
		String passcode = "", passwd ="", awdref ="";
*/		

	@PreAuthorize("hasAuthority('admin')")
	@RequestMapping(value="/downloadRankingForHighAwardReviewData", method = RequestMethod.GET)
    public void downloadRankingForHighAwardReviewData(Principal principle,
    		HttpServletResponse response, String passcode, String passwd, 
    		String awdref) throws IOException, SQLException {		
		if (isAuthorizedForReports(principle, passcode, passwd )) {		
			downloadDataAsCSV(response, 
				"SELECT CASE WHEN ( EDUAPPLICATION.STUDENT_ID in " 
					+ "		(SELECT ap.STUDENT_ID FROM EDUAPPLICATION ap "
					+ "	 	  inner join EDUAPP_PROCESS_DETAIL p on ap.ID = p.EDUAPP_ID "
					+ "			and ap.APPLICATION_YEAR <  (select top 1 APP_YEAR from EDUAPP_CONFIG)  and p.AWARD_AMOUNT > 250) )"
					+ "		   THEN 1 ELSE 0 END PreviousHighAward,"
					+ " (CAST ((1.0- ((CASE WHEN (a.REVIEWED_ANNUAL_FAMILY_INCOME -25000) < 0 THEN 0 "
					+ "		WHEN  (a.REVIEWED_ANNUAL_FAMILY_INCOME <= 360000) then a.REVIEWED_ANNUAL_FAMILY_INCOME - 25000"
					+ "		ELSE 360000 end) /360000.0))  as decimal(5,4)) *0.2) + "
					+ " (CAST((CASE WHEN a.REVIEWED_ANNUAL_FAMILY_INCOME <a.REVIEWED_ANNUAL_TUTION_FEE THEN 1 "
					+ "		        WHEN a.REVIEWED_ANNUAL_FAMILY_INCOME < 25000 THEN 1 "
					+ "             ELSE ((1.0*a.REVIEWED_ANNUAL_TUTION_FEE)/a.REVIEWED_ANNUAL_FAMILY_INCOME) end) as decimal(5,4)) *0.2) + "
					+ " (CAST(((isNull(REVIEWER_PREF_SB, 0) + isNull(REVIEWER_PREF_SP, 0) + isNULL(REVIEWER_PREF_BL, 0) + "	
					+ "      isNull(REVIEWER_PREF_AC, 0) + isNull(REVIEWER_PREF_SS, 0) + isNull(REVIEWER_PREF_GP,0))/6.0) as decimal(5,4)) *0.15) +"
					+ " (CAST((REVIEWED_MARK_PERCENT /100.0) as decimal(5,4)) * 0.45) TotalScore, "
					+ " concat(EDUAPPLICATION.FIRST_NAME, ' ', EDUAPPLICATION.LAST_NAME) StudentName, "
					+ " EDUAPPLICATION.STUDENT_ID, "
					+ " EDUAPPLICATION.CONFIRMATION_NMBR,"
					+ " EDUAPPLICATION.DEGREE, "
					+ " EDUAPPLICATION.COLLEGE_YEAR, "
					+ " a.RETURNING_STUDENT AS RECOMMEND_TO_MULTIYEAR, "
					+ " a.REVIEWED_MARK_PERCENT, "
					+ " a.REVIEWED_ANNUAL_FAMILY_INCOME, "
					+ " a.REVIEWED_ANNUAL_TUTION_FEE, "
					+ " case when (a.REVIEWED_ANNUAL_FAMILY_INCOME -25000) < 0 then 0 "
					+ "    when (a.REVIEWED_ANNUAL_FAMILY_INCOME <= 360000) then "
					+ "        a.REVIEWED_ANNUAL_FAMILY_INCOME - 25000 else 360000 end ADJUSTED_INCOME, "
					+ "	isNull(a.REVIEWER_PREF_SB, 0) + isNull(a.REVIEWER_PREF_SP, 0) + isNULL(a.REVIEWER_PREF_BL, 0) +" 
					+ "	isNull(REVIEWER_PREF_AC, 0) + isNull(REVIEWER_PREF_SS, 0) + isNull(REVIEWER_PREF_GP,0) AS REVIEWER_PREF_PERCENT, "
					+ " a.EXCEPTION AS TO_BE_DISCUSSED, "
					+ "	case when REVIEWER_PREF_BL = 1 then 'BL' else null end BL, "
					+ "	case when REVIEWER_PREF_SP = 1 then 'SP' else null end SP, "
					+ "	case when REVIEWER_PREF_SS = 1 then 'SS' else null end SS, "
					+ "	case when REVIEWER_PREF_SB = 1 then 'SB' else null end SB, "
					+ " a.REVIEWER, "
					+ " a.REVIEWER_PROCESSING_COMMENT   "
					+ "  from  EDUAPP_PROCESS_DETAIL a "
					+ " inner join EDUAPPLICATION on a.EDUAPP_ID = EDUAPPLICATION.ID "
					+ " where EDUAPPLICATION.APPLICATION_YEAR = (select top 1 APP_YEAR from EDUAPP_CONFIG) "
					+ "   and a.PROCESSING_STATUS = 'ReviewComplete' order by 2 desc",
					"HighAwardReview");
		} else {
			downloadDataAsCSV(response,"SELECT ''","blank");
		}			
	}
	
	
	@PreAuthorize("hasAuthority('admin')")
	@RequestMapping(value="/downloadAwardMailingAddressData", method = RequestMethod.GET)
    public void downloadAwardMailingAddressData(Principal principle,
    		HttpServletResponse response, String passcode, String passwd, 
    		String awdref) throws IOException, SQLException {		
		if (isAuthorizedForReports(principle, passcode, passwd )) {		
			downloadDataAsCSV(response, 
				"SELECT concat(EDUAPPLICATION.FIRST_NAME, ' ', EDUAPPLICATION.LAST_NAME) \"Student Name\", " + 
						"  EDUAPPLICATION.STUDENT_ID, " + 
						"  a.AWARD_AMOUNT, " + 
						"  EDUAPPLICATION.CONFIRMATION_NMBR, " + 
						"  a.P_Beneficiary_ADDRESS_LINE1 as ADDRESS_LINE1, " + 
						"  a.P_Beneficiary_ADDRESS_LINE2 as ADDRESS_LINE2, " +
						"  a.P_Beneficiary_ADDRESS_LINE3 as ADDRESS_LINE3_0, " + 						
						"  CASE WHEN (substring(EDUAPPLICATION.PIN,1,2) in ('60', '61', '62', '63')) THEN concat(EDUAPPLICATION.CITY, ' TN ', EDUAPPLICATION.PIN) " + 
						"             WHEN (substring(EDUAPPLICATION.PIN,1,2) in ('51', '52', '53')) THEN concat(EDUAPPLICATION.CITY,' AP ',  EDUAPPLICATION.PIN) " +  
						"             WHEN (substring(EDUAPPLICATION.PIN,1,2) in ('56', '57', '58', '59')) THEN concat(EDUAPPLICATION.CITY,' KA ',  EDUAPPLICATION.PIN) " +  
						"             WHEN (substring(EDUAPPLICATION.PIN,1,2) in ('67', '68', '69')) THEN concat(EDUAPPLICATION.CITY, ' KL ', EDUAPPLICATION.PIN) " + 
						"             ELSE concat( EDUAPPLICATION.CITY, ' ', EDUAPPLICATION.PIN)  END ADDRESS_LINE3, " + 
						"  EDUAPPLICATION.EMAIL " + 
						"  from  EDUAPP_PROCESS_DETAIL a " + 
						" inner join EDUAPPLICATION on a.EDUAPP_ID = EDUAPPLICATION.ID " + 
						" where EDUAPPLICATION.APPLICATION_YEAR = (select top 1 APP_YEAR from EDUAPP_CONFIG) " + 
						"   and a.PROCESSING_STATUS = 'Awarded' " + 
						"    and a.CHECK_NUMBER = '" + awdref + "'" +						
						" order by 1",
						"Award-MailingAddress"+awdref);
		} else {
			downloadDataAsCSV(response,"SELECT ''","blank");
		}			
	}
	
	@PreAuthorize("hasAuthority('admin')")
	@RequestMapping(value="/downloadBankTransferInstructionData", method = RequestMethod.GET)
    public void downloadBankTransferInstructionData(Principal principle,
    		HttpServletResponse response, String passcode, String passwd, 
    		String awdref) throws IOException, SQLException {
		if (isAuthorizedForReports(principle, passcode, passwd )) {		
			downloadDataAsCSV(response, 
					"select concat('ENW17',replace(STUDENT_ID,'-','')) EDUFX" + getCurrentDate() +
					   " ,null as \"Contract ID\" " +
					   ",'INR' as \"Buy Currency Code\" " +
					   ",null as \"Buy Currency Amount\" " +
					   ",'USD' as \"Sell Currency Code\" " +
					   ",a.AWARD_AMOUNT  as \"Sell Currency Amount \" " +
					   ",null as \"Start Date\" " +
					   ",'tom' \"Value Date OR Value Date Type\" " +
					   ",null as \"SWAP REFERENCE\" " +
					   ",'Account' as \"Funding Type \" " +
					   ",5720705713 as \"Funding Account Number\" " +
					   ",null as \"Funding Reference ID \" " +
					   ",null as \"Separate Debits\" " +
					   ",'Corpwire' as \"Payment Type\" " +
					   ",null as \"Payment Account Number\" " +
					   ",null as \"Payment Reference ID\" " +
					   ",null as \"Add Beneficiary Template\" " +
					   ",null as \"Beneficiary Swift Code\" " +

					   ",CHECK_TO_NAME as \"Beneficiary Name\" " +
					   ",a.P_BENEFICIARY_ADDRESS_LINE1 as \"Beneficiary Address1\" " +
					   ",a.P_BENEFICIARY_ADDRESS_LINE2  as \"Beneficiary Address2\" " +
					   ",a.P_BENEFICIARY_ADDRESS_LINE3 as \"Beneficiary Address3\" " +
					   ",'IN' as \"Beneficiary Country\" " +
					   ",concat('#', ACCOUNT_NUMBER)  as \"Beneficiary Account Number\" " +
					   ",CASE WHEN a.USE_SWIFT = 'Y'  THEN replace(replace(a.P_BANK_SWIFT_CODE, ' ', ''), '-', '') ELSE null END  as \"Beneficiary Bank Swift Code\" " +
					   ",CASE WHEN a.USE_SWIFT = 'Y'  THEN null ELSE a.P_BANK_NAME END  as \"Beneficiary Bank Name\" " +
					   ",CASE WHEN a.USE_SWIFT = 'Y' THEN null ELSE a.P_BRANCH_ADDRESS_LINE1 END as \"Beneficiary Bank Address1\" " +
					   ",CASE WHEN a.USE_SWIFT = 'Y' THEN null ELSE a.P_BRANCH_ADDRESS_LINE2 END  as \"Beneficiary Bank Address2\" " +
					   ",CASE WHEN a.USE_SWIFT = 'Y' THEN null ELSE a.P_BRANCH_ADDRESS_LINE3 END as \"Beneficiary Bank Address3\" " +
					   ",CASE WHEN a.USE_SWIFT = 'Y' THEN null ELSE 'IN' END as \"Beneficiary Bank Country\" " +
					   ",null as \"Beneficiary Bank Account\" " +
					   ",replace(BRANCH_IFSC_CODE, ' ', '') as \"Bank Routing Code (IFSC)\" " +
					   ",null as \"Intermediary Bank SWIFT Code\" " +
					   ",null as \"Intermediary Bank Name\" " +
					   ",null as \"Intermediary Bank Account\" " +
					   ",null as \"Intermediary Bank Address1\" " +
					   ",null as \"Intermediary Bank Address2\" " +
					   ",null as \"Intermediary Bank Address3\" " +
					   ",null as \"Contact Name\" " +
					   ",null as \"Contact Phone\" " +
					   ",null as \"Payment Purpose\" " +
					   ",'nsnatreasurer@achi.org' as \"emailAddress1\" " +
					   ",'nsnaedu@achi.org' as \"emailAddress2\" " +
					   ",null as \"emailAddress3\" " +
					   ",null as \"emailAddress4\" " +
					   ",null as \"emailAddress5\" " +
					   ",null as \"emailAddress6\" " +
					   ",null as \"emailAddress7\" " +
					   ",null as \"emailAddress8\" " +
					   ",null as \"emailAddress9\" " +
					   ",null as \"emailAddress10\" " +
					   ",'Education Scholarship' as \"Payment Details1 (Purpose)\" " +
					   ",'TO A NEEDY Deserving Student' as \"Payment Details2\" " +
					   ",concat('IFSC: ', BRANCH_IFSC_CODE) as \"Payment Details3 (IFSC)\" " +
					   ",concat('Acc: ', ACCOUNT_NUMBER) as \"Payment Details4 (Account #)\" " +
					   ",null as \"Reference Info1 (Application ID)\" " +
					   ",null as \"Reference Info2\" " +
					   ",null as \"Reference Info3\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ",null as \"Blank\" " +
					   ", concat('ENW17',replace(STUDENT_ID,'-',''))  as \"Record ID End\" " +

					   ",length(a.P_Beneficiary_ADDRESS_LINE1) " +
					   ",length(a.P_Beneficiary_ADDRESS_LINE2) " +

				"	  from EDUAPPLICATION  " +
				"	 inner join EDUAPP_PROCESS_DETAIL a on a.EDUAPP_ID = EDUAPPLICATION.ID " + 
				"	where EDUAPPLICATION.APPLICATION_YEAR = (select top 1 APP_YEAR from EDUAPP_CONFIG) " + 
				"	 and a.PROCESSING_STATUS = 'Awarded' " +
				"    and a.CHECK_NUMBER = '" + awdref + "'" +
				"	  and EDUAPPLICATION.BRANCH_IFSC_CODE is not null " + 
				"	    order by EDUAPPLICATION.BRANCH_IFSC_CODE" , 
						awdref); 
		} else {
			downloadDataAsCSV(response,"SELECT ''","blank");
		}			
	}	
	
	
	private boolean isAuthorizedForReports(Principal principle, String passcode, String passwd) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		User loginUser = userRepository.findByUserName(principle.getName());		
		
		List<EduappConfig> eduappConfigList = eduappConfigRepository.findAll();
		String  configPasscodeHash = eduappConfigList.get(0).getPasscodeHash();		
		
		// if the provided passcode match config passcode and 
		// passed matches the user password in the db, run the report.
		if (/*passwordEncoder.matches(passwd, loginUser.getPasswordHash())
			&& */
			passwordEncoder.matches(passcode, configPasscodeHash)) {	
			return true;
		} else {
			return false;
		}
	}
	
    private void downloadDataAsCSV(HttpServletResponse response, String reportSql, String filename) throws IOException, SQLException {
		Connection conn = dataSource.getConnection();;
	    Statement stat = conn.createStatement();
	    
	    ResultSet rs = stat.executeQuery(reportSql);
	    
 		String tempFolder = appParameterService.getTempDownloadFolder();
 		String outPath = tempFolder + "\\" +filename + "-" + getCurrentDate() + ".csv" ;
	    //String out = "C:/Users/Somasundaram/gitrepo/NSNAEduApp/NSNAEduApp/test.csv";
        BufferedWriter writer = new BufferedWriter(new FileWriter(outPath));
        Csv csv = new Csv();
        csv.setFieldDelimiter((char) '"');
        csv.setLineSeparator("\n");
        csv.write(writer, rs);
        conn.close();
	    
	    String mimeType= "application/vnd.ms-excel";

        response.setContentType(mimeType);		
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" 
        				+ filename + "-" + getCurrentDate() +".csv" +"\""));
        InputStream inputStream = FileUtils.newInputStream(outPath);
        response.setContentLength((int)FileUtils.size(outPath));
        

        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }		
    
    private String getCurrentDate() {
    	DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        Date date = new Date();
        return dateFormat.format(date);    	
    }

}
