package org.nsna.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ReviewerProgress implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3381705133031114020L;
	private String reviewer;
	private Integer assignedCount;
	private Integer completedCount;
	
	public ReviewerProgress() {		
	}
	
	public ReviewerProgress(String reviewer, Integer assignedCount, Integer completedCount){
		this.reviewer = reviewer;
		this.assignedCount = assignedCount;
		this.completedCount = completedCount;
	}
	
	@Id
	@Column(name = "REVIEWER", length = 25)
	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	
	@Column(name = "AssignedCount")
	public Integer getAssignedCount() {
		return assignedCount;
	}
	public void setAssignedCount(Integer assignedCount) {
		this.assignedCount = assignedCount;
	}
	
	@Column(name = "CompletedCount")	
	public Integer getCompletedCount() {
		return completedCount;
	}
	public void setCompletedCount(Integer completedCount) {
		this.completedCount = completedCount;
	}
}
