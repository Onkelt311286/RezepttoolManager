package de.tkoehler.rezepttool.manager.services.exceptions;

public class EditorServiceException extends Exception {
	private static final long serialVersionUID = -418354059771392247L;

	public EditorServiceException() {
	}

	public EditorServiceException(String arg0) {
		super(arg0);
	}

	public EditorServiceException(Throwable arg0) {
		super(arg0);
	}

	public EditorServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public EditorServiceException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
}
