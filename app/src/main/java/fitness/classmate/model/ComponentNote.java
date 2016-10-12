package fitness.classmate.model;

import android.os.Parcel;
import android.os.Parcelable;

import fitness.classmate.util.Helpbot;

public class ComponentNote implements Parcelable {

	private String mMessage;
	private long mTimestamp;

	public ComponentNote() { }

	protected ComponentNote(Parcel in) {
		mMessage = in.readString();
		mTimestamp = in.readLong();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mMessage);
		dest.writeLong(mTimestamp);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ComponentNote> CREATOR = new Creator<ComponentNote>() {

		@Override
		public ComponentNote createFromParcel(Parcel in) {
			return new ComponentNote(in);
		}

		@Override
		public ComponentNote[] newArray(int size) {
			return new ComponentNote[size];
		}
	};

	public String getMessage() {
		return mMessage;
	}

	public void setMessage(String message) {
		mMessage = message;
	}

	public long getTimestamp() {
		return mTimestamp;
	}

	public void setTimestamp(long timestamp) {
		mTimestamp = timestamp;
	}

	@Override
	public boolean equals(Object obj) {
		try {
			ComponentNote otherNote = (ComponentNote) obj;
			return Helpbot.equalsWithNull(((ComponentNote) obj).getMessage(), mMessage) && otherNote.getTimestamp() == mTimestamp;
		} catch(Exception e) {
			return false;
		}
	}
}
