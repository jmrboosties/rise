package fitness.classmate.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import fitness.classmate.database.DatabaseObject;
import fitness.classmate.util.Helpbot;

import java.util.ArrayList;

public class ClassmateClass implements Parcelable, DatabaseObject {

	private int mId;

	private String mTitle;

	private String mAuthor;

	private String mCreatedAt;

	private ArrayList<ClassComponent> mComponents = new ArrayList<>();

	public ClassmateClass() { }

	protected ClassmateClass(Parcel in) {
		mComponents = in.createTypedArrayList(ClassComponent.CREATOR);
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

	public ArrayList<ClassComponent> getComponents() {
		return mComponents;
	}

	public void setComponents(@NonNull ArrayList<ClassComponent> components) {
		mComponents = components;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getAuthor() {
		return mAuthor;
	}

	public void setAuthor(String author) {
		mAuthor = author;
	}

	public String getCreatedAt() {
		return mCreatedAt;
	}

	public void setCreatedAt(String createdAt) {
		mCreatedAt = createdAt;
	}

	public String getDuration() {
		long time = 0;

		for(ClassComponent component : mComponents)
			time += component.getDuration();

		return Helpbot.getDurationTimestampFromMillis(time);
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}
}
