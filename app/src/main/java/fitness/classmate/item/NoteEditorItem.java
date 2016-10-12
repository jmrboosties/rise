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

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NoteEditorItem) {
			if(((NoteEditorItem) obj).getType() == mType) {
				if(mType == COMPONENT)
					return ((NoteEditorItem) obj).getComponent().equals(mComponent);
				else if(mType == NOTE)
					return ((NoteEditorItem) obj).getComponentNote().equals(mComponentNote);
				else if(mType == ADD_NOTE_BUTTON)
					return true;
				else
					throw new IllegalArgumentException("unrecognized type");
			}
			else
				return false;
		}
		else
			return false;
	}

}
