package org.nsna.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EduappConfigRepository extends JpaRepository<EduappConfig, Long> {
	
	List<EduappConfig> findByRegion(String region);

}






