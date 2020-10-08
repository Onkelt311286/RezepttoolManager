package de.tkoehler.rezepttool.manager.services.exceptions;

public class ImporterServiceException extends Exception {
	private static final long serialVersionUID = -418354059771392247L;

	public ImporterServiceException() {
	}

	public ImporterServiceException(String arg0) {
		super(arg0);
	}

	public ImporterServiceException(Throwable arg0) {
		super(arg0);
	}

	public ImporterServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ImporterServiceException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
}
