package org.nsna.domain;

public class ChangePasswdModal {
	private String currentPasswd;
	private String newPasswd;
	
	public ChangePasswdModal() {}
	
	public ChangePasswdModal (String currentPasswd, String newPasswd) {
		this.currentPasswd = currentPasswd;
		this.newPasswd = newPasswd;
	} 
	public String getCurrentPasswd() {
		return currentPasswd;
	}
	public void setCurrentPasswd(String currentPasswd) {
		this.currentPasswd = currentPasswd;
	}
	public String getNewPasswd() {
		return newPasswd;
	}
	public void setNewPasswd(String newPasswd) {
		this.newPasswd = newPasswd;
	}
	
}

