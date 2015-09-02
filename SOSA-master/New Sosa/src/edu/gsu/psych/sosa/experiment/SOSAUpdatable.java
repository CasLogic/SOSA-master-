package edu.gsu.psych.sosa.experiment;

public interface SOSAUpdatable {
	/** The normal cursor that the operating system uses. */
	public final static int CURSOR_NORMAL = 0;
	/** The cursor that shows that an object is grab-able. */
	public final static int CURSOR_GRABBER = 1;
	/** The cursor that shows that an object is being grabbed. */
	public final static int CURSOR_GRABBING = 2;
	
	void updateList();
	void updateOrderDropdown();
	void updateListActivePeg();
	void updateCursor(int cursorType);
}
