package org.nsna.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.nsna.domain.EduappConfig;
import org.nsna.domain.EduappConfigRepository;
import org.nsna.domain.EduappProcessDetailRepository;
import org.nsna.domain.Eduapplication;
import org.nsna.domain.EduapplicationRepository;
import org.nsna.domain.User;
import org.nsna.domain.UserRepository;
import org.nsna.domain.AppRank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
	@Autowired
	private DataSource dataSource;	
	
	@Autowired
	private EduapplicationRepository eduapplicationRepository;
	
	@Autowired
	private EduappConfigRepository eduappConfigRepository;
	
	@Autowired
	private EduappProcessDetailRepository eduappProcesDetailRepository;	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EntityManager em;	

	@PreAuthorize("hasAuthority('admin')")	
	@RequestMapping(value = "/saveAppConfig", method = RequestMethod.POST)
	public boolean saveAppConfig(@RequestBody EduappConfig eduappConfig) throws SQLException {
		Connection conn = dataSource.getConnection();;
	    Statement stat = conn.createStatement();
	    ResultSet rs = stat.executeQuery("select count(*) APPCOUNT from EDUAPPLICATION where APPLICATION_YEAR > " + eduappConfig.getAppYear());
	    rs.next();
	    int futureAppCount = rs. getInt("APPCOUNT");
	    conn.close();
	    if (futureAppCount==0) {
	    	eduappConfigRepository.save(eduappConfig);
	    	return true;
	    } else {
	    	return false;
	    }
	}
	
	@PreAuthorize("hasAuthority('admin')")	
	@RequestMapping(value = "/clearRanking", method = RequestMethod.POST)
	public void clearRanking() {
		eduappProcesDetailRepository.clearCurrentYearScoresAndRanking();
	}	
	
		
	@PreAuthorize("hasAuthority('admin')")	
	@RequestMapping(value = "/rankApplication", method = RequestMethod.POST)
	public void rankApplication() {
		eduappProcesDetailRepository.setCurrentYearScores();
		eduappProcesDetailRepository.setCurrentYearTotalScore();
		Query q = em.createNativeQuery(
				"select rownum() Rank, EDUAPP_ID \"Edu_App_Id\"  from ( "
				+ "		select TOTAL_SCORE, EDUAPP_ID "
				+ "		from EDUAPP_PROCESS_DETAIL "
				+ "		 WHERE EDUAPP_ID IN (SELECT ID FROM EDUAPPLICATION WHERE APPLICATION_YEAR = (SELECT top 1 APP_YEAR FROM EDUAPP_CONFIG)) "
				+ "		     AND PROCESSING_STATUS IN ('ReviewComplete','Approved','Awarded') "
				+ "		     AND TOTAL_SCORE  is NOT NULL "
				+ "		order by TOTAL_SCORE  desc)  ", AppRank.class);
		List<AppRank> ranklist = q.getResultList();
		for (AppRank r : ranklist) {
			eduappProcesDetailRepository.updateRank(r.getRank(), r.getEduAppId());
	//		System.out.println(r.getEduAppId().toString() + ": " + r.getRank().toString());
		}
	}
	
	@PreAuthorize("hasAuthority('admin')")	
	@RequestMapping(value = "/setDefaultAwardAmount", method = RequestMethod.POST)
	public void setDefaultAwardAmount() {
		eduappProcesDetailRepository.setDefaultAwardAmount();
	}
	
	@PreAuthorize("hasAuthority('admin')")	
	@RequestMapping(value = "/setStatusToAwardedForApprovedWithBankDetails", method = RequestMethod.POST)
	public void setStatusToAwardedForApprovedWithBankDetails() {	
		eduappProcesDetailRepository.setStatusToAwardedForApprovedWithBankDetails();
	}
	
	@PreAuthorize("hasAuthority('admin')")	
	@RequestMapping(value = "/loadApplicationsForAwdAdmin", method = RequestMethod.GET)
	public List<Eduapplication> loadApplicationsForAwdAdmin() {
		List<Eduapplication> results = eduapplicationRepository.findApplicationsForAwardAdmin();
		return results;
	}	
	
	
	@PreAuthorize("hasAuthority('admin')")	
	@RequestMapping(value = "/getUserMgmtList", method = RequestMethod.GET)
	public List<User> getUserMgmtList() {
		List<User> results = userRepository.findAllByOrderByEndDateAscUserNameAsc();
		return results;

	}	
	
	@PreAuthorize("hasAuthority('admin')")	
	@RequestMapping(value = "/getUser", method = RequestMethod.GET)
	public User getUser(@RequestParam("userId") long userId) {
		User user;
		if (userId == -1) {
			user = new User();
		} else {
			user = userRepository.findOne(userId);
		}
		return user;
	}	
	
	@PreAuthorize("hasAuthority('admin')")	
	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public int saveUser(@RequestBody User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if (user.getId() == null){
			//New User
			
			if( userRepository.findByUserName(user.getUserName()) != null){
				return -1; //username already exists.
			};
			
			//Set default Password
			user.setPasswordHash(passwordEncoder.encode("N$naEduUs3r"));
		}
		userRepository.save(user);
		return 0;
	}	
	
}


