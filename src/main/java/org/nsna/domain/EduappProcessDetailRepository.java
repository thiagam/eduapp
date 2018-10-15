package org.nsna.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EduappProcessDetailRepository extends JpaRepository<EduappProcessDetail, Long> {
	
	@Query(value = "select a.* from EDUAPP_PROCESS_DETAIL a "
			  + "inner join EDUAPPLICATION b on a.EDUAPP_ID  = b.ID"
			  + " where b.CONFIRMATION_NMBR = :applicationNmbr "
			  , nativeQuery = true)
	List<EduappProcessDetail> findByApplicationNmbr(
			@Param("applicationNmbr") String applicationNmbr);
	
	@Query(value = "select a.* from EDUAPP_PROCESS_DETAIL a "
			  + "inner join EDUAPPLICATION b on a.EDUAPP_ID  = b.ID"
			  + " where b.student_id = :studentId "
			  + "  and b.birthdate = :birthdate"
			  + " order by b.APPLICATION_YEAR  desc"
			  , nativeQuery = true)
	List<EduappProcessDetail> findByStudentIdAndBirthdate(
			@Param("studentId") String studentId,
			@Param("birthdate") Date birthdate);	
	
	@Modifying
	@Transactional
	@Query(value = "update EDUAPP_PROCESS_DETAIL  set RANK = :rank where EDUAPP_ID = :eduAppId", nativeQuery = true)
	int updateRank(
			@Param("rank") Short rank, 
			@Param("eduAppId") Long eduAppId);
	
	@Modifying
	@Transactional
	@Query(value = "update EDUAPP_PROCESS_DETAIL  " +
					  "set MARK_SCORE = null, INCOME_SCORE = null, " +
					   	   " REVIEWER_SCORE = null, TOTAL_SCORE = null, " +
					   	   " RANK = null " +
					" where EDUAPP_ID IN (SELECT ID FROM EDUAPPLICATION WHERE APPLICATION_YEAR = (SELECT top 1 APP_YEAR FROM EDUAPP_CONFIG)) " +
						" AND RANK is NOT NULL" +
						" AND PROCESSING_STATUS = 'ReviewComplete' ", nativeQuery = true)
	int clearCurrentYearScoresAndRanking();	
	
	@Modifying
	@Transactional
	@Query(value = "update EDUAPP_PROCESS_DETAIL  " +
					  "set MARK_SCORE = (REVIEWED_MARK_PERCENT /100.0) , " +
					  	" INCOME_SCORE = cast ((1.0- ((case when (REVIEWED_ANNUAL_FAMILY_INCOME -25000) < 0 then 0 " +
					  		"when  (REVIEWED_ANNUAL_FAMILY_INCOME <= 360000) then REVIEWED_ANNUAL_FAMILY_INCOME - 25000 else " +
					  			  " 360000   end) /360000.0))  as decimal(5,4)) , " +
					  	" REVIEWER_SCORE = cast(((isNull(REVIEWER_PREF_SB, 0) + isNull(REVIEWER_PREF_SP, 0) + isNULL(REVIEWER_PREF_BL, 0)+"+
					  			  "isNull(REVIEWER_PREF_AC, 0) + isNull(REVIEWER_PREF_SS, 0) + isNull(REVIEWER_PREF_GP,0))/6.0) as decimal(5,4)) " +
					" where EDUAPP_ID IN (SELECT ID FROM EDUAPPLICATION WHERE APPLICATION_YEAR = (SELECT top 1 APP_YEAR FROM EDUAPP_CONFIG)) " +
						" AND PROCESSING_STATUS = 'ReviewComplete' " /*+
						" AND REVIEWER_PREF_PERCENT  <= 6"*/, nativeQuery = true)
	int setCurrentYearScores();	
	
	@Modifying
	@Transactional
	@Query(value = "update EDUAPP_PROCESS_DETAIL  " +
					  "     set TOTAL_SCORE = (MARK_SCORE * 0.55) + (INCOME_SCORE * 0.3) + (REVIEWER_SCORE * 0.15) " +
					" where EDUAPP_ID IN (SELECT ID FROM EDUAPPLICATION WHERE APPLICATION_YEAR = (SELECT top 1 APP_YEAR FROM EDUAPP_CONFIG)) " +
						" AND PROCESSING_STATUS = 'ReviewComplete' " /* +
						" AND REVIEWER_PREF_PERCENT  <= 6"*/, nativeQuery = true)
	int setCurrentYearTotalScore();		
	
	@Modifying
	@Transactional
	@Query(value =	"update EDUAPP_PROCESS_DETAIL " +
					"   set AWARD_AMOUNT  = (SELECT top 1 DEFAULT_AWARD_AMOUNT FROM EDUAPP_CONFIG), PROCESSING_STATUS = 'Approved' " +
					" WHERE EDUAPP_ID IN (SELECT ID FROM EDUAPPLICATION WHERE APPLICATION_YEAR = (SELECT top 1 APP_YEAR FROM EDUAPP_CONFIG)) " +
					"   AND PROCESSING_STATUS = 'ReviewComplete' ",
					nativeQuery = true)
	int setDefaultAwardAmount();
	
	@Modifying
	@Transactional
	@Query(value =	"update EDUAPP_PROCESS_DETAIL " +
					"   set CHECK_NUMBER = concat('EDUFX' , FORMATDATETIME(CURRENT_TIMESTAMP(), 'YYMMdd')), " +
					"	PROCESSING_STATUS = 'Awarded' " +
					" WHERE EDUAPP_ID IN (SELECT ID FROM EDUAPPLICATION WHERE APPLICATION_YEAR = (SELECT top 1 APP_YEAR FROM EDUAPP_CONFIG) " +
					"			AND EDUAPPLICATION.BRANCH_IFSC_CODE is not null) " +
					"   AND PROCESSING_STATUS = 'Approved' " +
					"   AND NOT ( " +
					"     (USE_SWIFT = 'Y' and not (length(isNull(P_BANK_SWIFT_CODE,''))  in (8, 11)) ) OR " +
					"     (isNull(USE_SWIFT,'N') = 'N' and  (length(isNull(P_BRANCH_ADDRESS_LINE1,'')) = 0 or length(isNull(P_BRANCH_ADDRESS_LINE3,'')) = 0) )) ",
					nativeQuery = true)
	int setStatusToAwardedForApprovedWithBankDetails();
	
}
