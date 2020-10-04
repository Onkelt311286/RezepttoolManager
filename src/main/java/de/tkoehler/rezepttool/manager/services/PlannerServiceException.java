package de.tkoehler.rezepttool.manager.services;

public class PlannerServiceException extends Exception {
	private static final long serialVersionUID = -4143997675575270618L;

	public PlannerServiceException() {
	}

	public PlannerServiceException(String arg0) {
		super(arg0);
	}

	public PlannerServiceException(Throwable arg0) {
		super(arg0);
	}

	public PlannerServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public PlannerServiceException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
}
