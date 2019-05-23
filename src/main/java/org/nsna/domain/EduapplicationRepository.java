package org.nsna.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EduapplicationRepository extends JpaRepository<Eduapplication, Long> {
	@EntityGraph(value = "EduApplWithProcessDetail", type = EntityGraphType.LOAD)
	List<Eduapplication> findByFirstNameLike(String firstName);

	@Query(value = "select * from Eduapplication a "
			  + "inner join Eduapp_Process_Detail b ON a.id = b.eduapp_id "	
			  + "where b.processing_Status = :status "
			  + "and a.region = :region"
			  , nativeQuery = true)
	List<Eduapplication> findByEduappProcessDetailProcessingStatus(
			@Param("status") String status,
			@Param("region") String region);
	
	@EntityGraph(value = "EduApplWithProcessDetail", type = EntityGraphType.LOAD)
	List<Eduapplication> findByApplicationYearAndRegion(String year, String region);
	
	@EntityGraph(value = "EduApplWithProcessDetail", type = EntityGraphType.LOAD)
	List<Eduapplication> findByEduappProcessDetailReviewerAndRegion(String reviewer, String region);
	
	@EntityGraph(value = "EduApplWithProcessDetail", type = EntityGraphType.LOAD)
	List<Eduapplication> findByApplicationYearAndEduappProcessDetailReviewerAndRegion(String year, String reviewer, String region);

	@EntityGraph(value = "EduApplWithProcessDetail", type = EntityGraphType.LOAD)
	List<Eduapplication> findByStudentIdAndBirthdateOrderByApplicationYearDesc(String studentId, Date birthdate);
	
	@EntityGraph(value = "EduApplWithProcessDetail", type = EntityGraphType.LOAD)
	List<Eduapplication> findByConfirmationNmbr(String confirmationNmbr);

	//find by 2015 check ref nmbr 
	@Query(value = "select * from Eduapplication a "
			+ "where a.APPLICATION_YEAR = '2015' "
			+ "  and  birthdate = 	:birthdate"
			+ "  and concat('EDU-NE-2015-',  cast(TEMP_EDU_ID as varchar)) = :checkRefNmbr2015 "
			  , nativeQuery = true)
	List<Eduapplication> findBy2015CheckRefNmbrAndBirthdate(
			@Param("checkRefNmbr2015") String checkRefNmbr2015,
			@Param("birthdate") Date birthdate);			
	
	
	/*
	@Query("select a from Eduapplication a "
		  + "where a.studentId <> :studentId "
		  + "  and a.applicationYear = :applicationYear"
		  + "  and (a.birthdate = :birthdate"
		  + "   or  (a.firstName = :firstName and a.lastName = :lastName)"
		  + "   or  a.email = :email"
		  + "   or  (a.fathersName = :fathersName and a.mothersName = :mothersName))")
	*/
	
	//possible duplicate in the same application Year
	@Query(value = "select * from Eduapplication a "
	//		  + "inner join Eduapp_Process_Detail b ON a.id = b.eduapp_id "
			  + "where a.application_Year = :applicationYear"
			  + "  and a.id <> :applID "
			  + "  and a.region = :region "
			  + "  and (a.student_Id = :studentId"
			  + "   or  a.birthdate = :birthdate"
			  + "   or  a.email = :email"			  
			  + "   or  ( replace(replace(a.first_Name,' ',''),'.', '') =  replace(replace(:firstName,' ',''), '.', '') "
			  + "           and replace(replace(a.last_Name,' ', ''), '.', '') = replace(replace(:lastName, ' ', ''), '.', '') )"
			  + "   or  ( replace(replace(a.fathers_Name, ' ', ''), '.', '') = replace(replace(:fathersName,' ', ''), '.', '') "
			  + "           and replace(replace(a.mothers_Name,' ',''), '.', '') = replace(replace(:mothersName,' ',''), '.', '') )"
			  + " )"
			  , nativeQuery = true)
	List<Eduapplication> findPossibleDuplicate(
			@Param("applID") Long applID,
			@Param("studentId") String studentId, 
			@Param("applicationYear") String applicationYear,
			@Param("birthdate") Date birthdate,
			@Param("email") String email,			
			@Param("firstName") String firstName ,
			@Param("lastName") String lastName ,
			@Param("fathersName") String fathersName,
			@Param("mothersName") String mothersName,
			@Param("region") String region);

	//possible applicant match in other year applications
	@Query(value = "select * from Eduapplication a "
			  + "inner join Eduapp_Process_Detail b ON a.id = b.eduapp_id "	
			  + "where a.application_Year <> :applicationYear and"
			  + "  a.region = :region and ("
			  + "  a.student_Id = :studentId "
			  + "  or  a.birthdate = :birthdate"
			  + "  or  a.email = :email"	
			  /*
			  + "  or (a.birthdate = :birthdate "
			  + "		and replace(replace(a.first_Name,' ',''),'.', '') =  replace(replace(:firstName,' ',''), '.', '') )"
			  + "   or  (a.email = :email"			  
			  + "   	and replace(replace(a.first_Name,' ',''),'.', '') =  replace(replace(:firstName,' ',''), '.', '') )"
			  */
			  + " ) order by a.application_Year"
			  , nativeQuery = true)
	List<Eduapplication> findPossibleApplicantOtherYearApps(
			@Param("studentId") String studentId, 
			@Param("applicationYear") String applicationYear,
			@Param("birthdate") Date birthdate,
			@Param("email") String email,
			@Param("region") String region
			//,@Param("firstName") String firstName
			);
	
	//possible duplicate in the same application Year
	@Query(value = "select * from Eduapplication a "
	//		  + "inner join Eduapp_Process_Detail b ON a.id = b.eduapp_id "
			  + "where a.application_Year = :applicationYear"
			  + "  and a.id <> :applID "
			  + "  and a.region <> :region "
			  + "  and (a.student_Id = :studentId"
			  + "   or  a.birthdate = :birthdate"
			  + "   or  a.email = :email"			  
			  + "   or  ( replace(replace(a.first_Name,' ',''),'.', '') =  replace(replace(:firstName,' ',''), '.', '') "
			  + "           and replace(replace(a.last_Name,' ', ''), '.', '') = replace(replace(:lastName, ' ', ''), '.', '') )"
			  + "   or  ( replace(replace(a.fathers_Name, ' ', ''), '.', '') = replace(replace(:fathersName,' ', ''), '.', '') "
			  + "           and replace(replace(a.mothers_Name,' ',''), '.', '') = replace(replace(:mothersName,' ',''), '.', '') )"
			  + " )"
			  , nativeQuery = true)
	List<Eduapplication> findPossibleDuplicateAcrossRegions(
			@Param("applID") Long applID,
			@Param("studentId") String studentId, 
			@Param("applicationYear") String applicationYear,
			@Param("birthdate") Date birthdate,
			@Param("email") String email,			
			@Param("firstName") String firstName ,
			@Param("lastName") String lastName ,
			@Param("fathersName") String fathersName,
			@Param("mothersName") String mothersName,
			@Param("region") String region);


	
	//partial search by Name or Emal or SudentId 
	@Query(value = "select * from Eduapplication a "
			  + "where (a.LAST_NAME like :searchString " 
			  + "   or a.FIRST_NAME like :searchString "
			  + "   or a.EMAIL like :searchString "
			  + "	or a.STUDENT_ID = :searchStudentId )"
			  + " and a.region= :region "
			  , nativeQuery = true)
	List<Eduapplication> findByNameIdEmail(
			@Param("searchString") String searchString, 
			@Param("searchStudentId") String searchStudentId,
			@Param("region") String region);	
	
	//applications in status 'ReviewComplete' or 'Approved' or 'Awarded' 
	@Query(value = "select * from Eduapplication a "
			  + "inner join Eduapp_Process_Detail b ON a.id = b.eduapp_id "	
			  + "where b.Processing_Status IN ('ReviewComplete', 'Approved', 'Awarded') " 
			  + "  and a.application_Year = (SELECT top 1 APP_YEAR FROM EDUAPP_CONFIG) "
			  + " and a.region = :region "
			  , nativeQuery = true)
	List<Eduapplication> findApplicationsForAwardAdmin(@Param("region") String region);	
	

	@Modifying
	@Transactional
	@Query(value = "update Eduapplication  set BANK_SWIFT_CODE = :swiftCode where CONFIRMATION_NMBR = :confirmationNmbr", nativeQuery = true)
	int updateSwiftCode(
			@Param("swiftCode") String swiftCode, 
			@Param("confirmationNmbr") String confirmationNmbr);	

}
