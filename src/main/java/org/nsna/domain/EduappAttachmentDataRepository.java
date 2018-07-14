package org.nsna.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EduappAttachmentDataRepository extends JpaRepository<EduappAttachmentData, Long> {

	EduappAttachmentData findByEduappAttachmentId(long attachmentId);
	

}
