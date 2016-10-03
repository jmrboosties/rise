package fitness.classmate.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class ClassmateClass implements Parcelable {

	private ArrayList<ClassmateClassComponent> mComponents = new ArrayList<>();

	public ClassmateClass() { }

	protected ClassmateClass(Parcel in) {
		mComponents = in.createTypedArrayList(ClassmateClassComponent.CREATOR);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(mComponents);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ClassmateClass> CREATOR = new Creator<ClassmateClass>() {

		@Override
		public ClassmateClass createFromParcel(Parcel in) {
			return new ClassmateClass(in);
		}

		@Override
		public ClassmateClass[] newArray(int size) {
			return new ClassmateClass[size];
		}
	};

	public ArrayList<ClassmateClassComponent> getComponents() {
		return mComponents;
	}

	public void setComponents(@NonNull ArrayList<ClassmateClassComponent> components) {
		mComponents = components;
	}

}
