package projetcloud.model;

public class QueryStateWrapper {
	
	private boolean success;
	
	public QueryStateWrapper(boolean success) {
		this.success = success;
	}
	
	public boolean getSuccess() {
		return success;
	}
}
