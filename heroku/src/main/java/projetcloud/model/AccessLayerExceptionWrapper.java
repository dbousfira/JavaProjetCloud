package projetcloud.model;

import projetcloud.database.AccessLayerException;

public class AccessLayerExceptionWrapper {

	private String error;
	
	public AccessLayerExceptionWrapper(AccessLayerException e) {
		this.error = e.getMessage();
	}
	
	public String getError() {
		return error;
	}
}
