package fitness.classmate.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ComponentNote implements Parcelable {

	private String mMessage;

	public ComponentNote() { }

	protected ComponentNote(Parcel in) {
		mMessage = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mMessage);
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
}
