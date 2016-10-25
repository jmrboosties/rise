package fitness.classmate.item;

import fitness.classmate.model.ClassComponent;
import fitness.classmate.model.ComponentNote;

public class ClassPlayerItem {

	private int mType;
	private ClassComponent mComponent;
	private ComponentNote mNote;

	public static final int COMPONENT = 0;
	public static final int NOTE = 1;

	public ClassComponent getComponent() {
		return mComponent;
	}

	public void setComponent(ClassComponent component) {
		mComponent = component;
	}

	public ComponentNote getNote() {
		return mNote;
	}

	public void setNote(ComponentNote note) {
		mNote = note;
	}

	public int getType() {
		return mType;
	}

	public void setType(int type) {
		mType = type;
	}

}
