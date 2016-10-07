package fitness.classmate.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import fitness.classmate.annotation.ColumnName;
import fitness.classmate.database.Tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ClassComponent implements Parcelable {

	@ColumnName(Tables.ClassmateClassComponents.NAME)
	private String mName;

	private ArrayList<ComponentNote> mComponentNotes = new ArrayList<>();

	private ComponentTrack mComponentTrack;

	//Only 1-5
	@ColumnName(Tables.ClassmateClassComponents.PACE)
	private int mIntensity = 3;

	public ClassComponent() { }

	protected ClassComponent(Parcel in) {
		mName = in.readString();
		mComponentNotes = in.createTypedArrayList(ComponentNote.CREATOR);
		mComponentTrack = in.readParcelable(ComponentTrack.class.getClassLoader());
		mIntensity = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeTypedList(mComponentNotes);
		dest.writeParcelable(mComponentTrack, flags);
		dest.writeInt(mIntensity);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ClassComponent> CREATOR = new Creator<ClassComponent>() {

		@Override
		public ClassComponent createFromParcel(Parcel in) {
			return new ClassComponent(in);
		}

		@Override
		public ClassComponent[] newArray(int size) {
			return new ClassComponent[size];
		}
	};

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public ArrayList<ComponentNote> getComponentNotes() {
		return mComponentNotes;
	}

	public void setComponentNotes(@NonNull ArrayList<ComponentNote> componentNotes) {
		mComponentNotes = componentNotes;
	}

	public void addComponentNote(@NonNull ComponentNote note) {
		mComponentNotes.add(note);
		Collections.sort(mComponentNotes, new Comparator<ComponentNote>() {

			@Override
			public int compare(ComponentNote o1, ComponentNote o2) {
				return (int) (o1.getTimestamp() - o2.getTimestamp());
			}

		});
	}

	public ComponentTrack getComponentTrack() {
		return mComponentTrack;
	}

	public void setComponentTrack(ComponentTrack componentTrack) {
		mComponentTrack = componentTrack;
	}

	public int getIntensity() {
		return mIntensity;
	}

	public void setIntensity(int intensity) {
		if(intensity < 1 || intensity > 5)
			throw new IllegalArgumentException("intensity is 1-5");

		mIntensity = intensity;
	}

	public long getDuration() {
		return mComponentTrack.getDuration(); //TODO need to adjust this
	}

}
