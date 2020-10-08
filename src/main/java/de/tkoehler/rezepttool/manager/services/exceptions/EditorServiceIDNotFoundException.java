package de.tkoehler.rezepttool.manager.services.exceptions;

public class EditorServiceIDNotFoundException extends EditorServiceException {

	private static final long serialVersionUID = 6173529157531325591L;

	public EditorServiceIDNotFoundException() {
	}

	public EditorServiceIDNotFoundException(String arg0) {
		super(arg0);
	}

	public EditorServiceIDNotFoundException(Throwable arg0) {
		super(arg0);
	}

	public EditorServiceIDNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public EditorServiceIDNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
}
