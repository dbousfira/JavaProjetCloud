package projetcloud.model;

public class DeleteResponseWrapper {

	private boolean success;
	
	public DeleteResponseWrapper(boolean success) {
		this.success = success;
	}
	
	public boolean getSuccess() {
		return success;
	}
}
