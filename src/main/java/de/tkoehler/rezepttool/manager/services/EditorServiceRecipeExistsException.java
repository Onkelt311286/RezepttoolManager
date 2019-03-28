package de.tkoehler.rezepttool.manager.services;

public class EditorServiceRecipeExistsException extends EditorServiceException {

	private static final long serialVersionUID = -5547702107656471940L;

	public EditorServiceRecipeExistsException() {
	}

	public EditorServiceRecipeExistsException(String arg0) {
		super(arg0);
	}

	public EditorServiceRecipeExistsException(Throwable arg0) {
		super(arg0);
	}

	public EditorServiceRecipeExistsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public EditorServiceRecipeExistsException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
}
