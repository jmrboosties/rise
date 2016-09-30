package fitness.classmate.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class ClassmateClass {

	private ArrayList<ClassmateClassComponent> mComponents = new ArrayList<>();

	public ArrayList<ClassmateClassComponent> getComponents() {
		return mComponents;
	}

	public void setComponents(@NonNull ArrayList<ClassmateClassComponent> components) {
		mComponents = components;
	}

}
