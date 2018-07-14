package org.nsna.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AppRank implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5640582009712921183L;
	/**
	 * 
	 */

	private Long eduAppId;	
	private Short rank;

	public AppRank() {		
	}
	
	public AppRank(Long eduAppId, Short rank){
		this.eduAppId = eduAppId;
		this.rank = rank;
	}

	@Id
	@Column (name = "EduAppId")
	public Long getEduAppId() {
		return eduAppId;
	}
	public void setEduAppId(Long eduAppId) {
		this.eduAppId = eduAppId;
	}
	
	@Column (name = "Rank")	
	public Short getRank() {
		return rank;
	}
	public void setRank(Short rank) {
		this.rank = rank;
	}	

}
