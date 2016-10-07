package fitness.classmate.item;

import fitness.classmate.model.ClassComponent;
import fitness.classmate.model.ComponentNote;

public class NoteEditorItem {

	public static final int COMPONENT = 0;
	public static final int NOTE = 1;
	public static final int ADD_NOTE_BUTTON = 2;

	private int mType;
	private ClassComponent mComponent;
	private ComponentNote mComponentNote;

	public int getType() {
		return mType;
	}

	public void setType(int type) {
		mType = type;
	}

	public ClassComponent getComponent() {
		return mComponent;
	}

	public void setComponent(ClassComponent component) {
		mComponent = component;
	}

	public ComponentNote getComponentNote() {
		return mComponentNote;
	}

	public void setComponentNote(ComponentNote componentNote) {
		mComponentNote = componentNote;
	}

}
