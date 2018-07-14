package org.nsna.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CatagoryReportData implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6158861425416944227L;
	private String catagory;
	private Integer count;
	
	public CatagoryReportData() {		
	}
	
	public CatagoryReportData(String catagory, Integer count){
		this.catagory = catagory;
		this.count = count;
	}
	
	@Id
	@Column(name = "Catagory", length = 25)	
	public String getCatagory() {
		return catagory;
	}
	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}
	
	@Column(name = "Count")	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}

}
