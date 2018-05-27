package projetcloud.database;

public class AccessLayerException extends Exception {
	
	public AccessLayerException(String details) {
		super(details);
	}
	
	public AccessLayerException(String details, Throwable origin) {
		super(details, origin);
	}
	
	public AccessLayerException(Throwable origin) {
		super(origin);
	}
}
