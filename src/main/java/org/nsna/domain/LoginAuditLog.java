package org.nsna.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * LoginAuditLog generated by hbm2java
 */
@Entity
@Table(name = "LOGIN_AUDIT_LOG", schema = "PUBLIC")
public class LoginAuditLog implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5887150590364748645L;
	private Long id;
	private String event;
	private String userName;
	private String requestIp;
	private String logMessage;
	private Date crTs;

	public LoginAuditLog() {
	}

	public LoginAuditLog(String event, String userName, String requestIp, String logMessage) {
		this.event = event;
		this.userName = userName;
		this.requestIp = requestIp;
		this.logMessage = logMessage;
	}

	public LoginAuditLog(String event, String userName, String requestIp, String logMessage, Date crTs) {
		this.event = event;
		this.userName = userName;
		this.requestIp = requestIp;
		this.logMessage = logMessage;
		this.crTs = crTs;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ID", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "EVENT", nullable = false, length = 25)
	public String getEvent() {
		return this.event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@Column(name = "USER_NAME", nullable = false, length = 25)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "REQUEST_IP", nullable = false, length = 30)
	public String getRequestIp() {
		return this.requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	@Column(name = "LOG_MESSAGE", nullable = false, length = 500)
	public String getLogMessage() {
		return this.logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CR_TS", length = 23)
	public Date getCrTs() {
		return this.crTs;
	}

	public void setCrTs(Date crTs) {
		this.crTs = crTs;
	}
	
	@PrePersist
	void createdAt() {
		this.crTs = new Date();
	}

}

