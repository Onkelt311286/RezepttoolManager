package de.tkoehler.rezepttool.manager.services;

public class ManagerServiceException extends Exception {
	private static final long serialVersionUID = -418354059771392247L;

	public ManagerServiceException() {
	}

	public ManagerServiceException(String arg0) {
		super(arg0);
	}

	public ManagerServiceException(Throwable arg0) {
		super(arg0);
	}

	public ManagerServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ManagerServiceException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
}
