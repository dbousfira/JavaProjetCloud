package projetcloud.model;

public class ExceptionWrapper {

	private String error;
	
	public ExceptionWrapper(Exception e) {
		this.error = e.getMessage();
	}
	
	public String getError() {
		return error;
	}
}
