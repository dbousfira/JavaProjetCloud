package projetcloud.model;

import java.io.Serializable;
import java.util.Objects;

public class Approval implements Serializable {

	private String name;
	private boolean approved;
	
	public Approval(String name, boolean approved) {
		this.name = name;
		this.approved = approved;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getApproved() {
		return approved;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != Approval.class) {
			return false;
		}
		Approval oApproval = (Approval) other;
		return oApproval.getName().equalsIgnoreCase(name);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
