package dtu.group2.Domain.Entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CustomerCreationRequest {
	private String accountNumber;
	private String sessionId;
	
	public String getAccountNumber() {
		return this.accountNumber;
	}
	
	public String getSessionId() {
		return this.sessionId;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
