package org.nsna.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findOneByUserName(String userName);

	User findByUserName(String userName);
	 
	 @Query(value ="SELECT * FROM User a " + "where a.region = :region " +
			 " and a.USER_NAME= :userName" , nativeQuery = true)
	User findByUserName( @Param("userName") String userName, @Param("region") String region);

	Optional<User> findByUserEmail(String email);
	Optional<User> findByResetToken(String resetToken);	
	List<User> findAll();
	
	List<User> findAllByOrderByEndDateAscUserNameAsc();
	List<User> findByRegionOrderByEndDateAscUserNameAsc(String region);
	
	//Active users -- users not ended and not hidden
	@Query(value = "select * from User a "
			  + "where IFNULL(a.END_DATE, '3333-03-03') > current_date() "
			  + "  and IFNULL(a.HIDDEN, 'N') = 'N' " 
			  + "  and a.region = :region " 
			  + "order by a.USER_NAME" 
			  , nativeQuery = true)
	List<User> findActiveUsers(@Param("region") String region);		
}

