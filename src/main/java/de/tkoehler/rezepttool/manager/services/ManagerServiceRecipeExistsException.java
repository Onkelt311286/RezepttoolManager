package de.tkoehler.rezepttool.manager.services;

public class ManagerServiceRecipeExistsException extends ManagerServiceException {
	private static final long serialVersionUID = 919239804065191844L;

	public ManagerServiceRecipeExistsException() {
	}

	public ManagerServiceRecipeExistsException(String arg0) {
		super(arg0);
	}

	public ManagerServiceRecipeExistsException(Throwable arg0) {
		super(arg0);
	}

	public ManagerServiceRecipeExistsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ManagerServiceRecipeExistsException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
}
