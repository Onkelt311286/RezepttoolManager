package de.tkoehler.rezepttool.manager.services;

public class ManagerServiceIDNotFoundException extends ManagerServiceException {

	private static final long serialVersionUID = -8358792906940539782L;

	public ManagerServiceIDNotFoundException() {
	}

	public ManagerServiceIDNotFoundException(String arg0) {
		super(arg0);
	}

	public ManagerServiceIDNotFoundException(Throwable arg0) {
		super(arg0);
	}

	public ManagerServiceIDNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ManagerServiceIDNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
}
