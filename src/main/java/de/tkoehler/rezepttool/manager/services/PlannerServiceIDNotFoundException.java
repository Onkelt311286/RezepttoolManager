package de.tkoehler.rezepttool.manager.services;

public class PlannerServiceIDNotFoundException extends PlannerServiceException {

	private static final long serialVersionUID = -2644690768098255852L;

	public PlannerServiceIDNotFoundException() {
	}

	public PlannerServiceIDNotFoundException(String arg0) {
		super(arg0);
	}

	public PlannerServiceIDNotFoundException(Throwable arg0) {
		super(arg0);
	}

	public PlannerServiceIDNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public PlannerServiceIDNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
}
