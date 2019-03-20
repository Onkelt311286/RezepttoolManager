package de.tkoehler.rezepttool.manager.services;

public class ImporterServiceRecipeExistsException extends ImporterServiceException {

	private static final long serialVersionUID = 919239804065191844L;

	public ImporterServiceRecipeExistsException() {
	}

	public ImporterServiceRecipeExistsException(String arg0) {
		super(arg0);
	}

	public ImporterServiceRecipeExistsException(Throwable arg0) {
		super(arg0);
	}

	public ImporterServiceRecipeExistsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ImporterServiceRecipeExistsException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
}
