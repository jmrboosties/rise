package fitness.classmate.item;

import fitness.classmate.model.ClassComponent;

public class ClassGraphItem {

	private ClassComponent mClassComponent;
	private int mType;

	public static final int COMPONENT = 0;
	public static final int PLACEHOLDER = 1;

	public int getType() {
		return mType;
	}

	public void setType(int type) {
		mType = type;
	}

	public ClassComponent getClassComponent() {
		return mClassComponent;
	}

	public void setClassComponent(ClassComponent classComponent) {
		mClassComponent = classComponent;
	}
}
