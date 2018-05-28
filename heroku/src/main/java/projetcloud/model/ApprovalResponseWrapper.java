package projetcloud.model;

import org.json.JSONObject;

public class ApprovalResponseWrapper {

	private boolean approved;
	private JSONObject account;
	
	public ApprovalResponseWrapper(JSONObject account, boolean approved) {
		this.account = account;
		this.approved = approved;
	}
	
	public JSONObject getAccount() {
		return account;
	}
	
	public boolean getApproved() {
		return approved;
	}
}
