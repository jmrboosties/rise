package fitness.classmate.item;

import fitness.classmate.model.ClassmateClassComponent;

public class ClassGraphItem {

	private ClassmateClassComponent mClassComponent;
	private int mType;

	public static final int COMPONENT = 0;
	public static final int PLACEHOLDER = 1;

	public int getType() {
		return mType;
	}

	public void setType(int type) {
		mType = type;
	}

	public ClassmateClassComponent getClassComponent() {
		return mClassComponent;
	}

	public void setClassComponent(ClassmateClassComponent classComponent) {
		mClassComponent = classComponent;
	}
}
