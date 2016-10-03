package fitness.classmate.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import fitness.classmate.constant.FieldNames;

public class SpotifyAudioFeatures implements Parcelable {

	@SerializedName(FieldNames.BPM)
	private float mBpm;

	protected SpotifyAudioFeatures(Parcel in) {
		mBpm = in.readFloat();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeFloat(mBpm);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<SpotifyAudioFeatures> CREATOR = new Creator<SpotifyAudioFeatures>() {

		@Override
		public SpotifyAudioFeatures createFromParcel(Parcel in) {
			return new SpotifyAudioFeatures(in);
		}

		@Override
		public SpotifyAudioFeatures[] newArray(int size) {
			return new SpotifyAudioFeatures[size];
		}
	};

	public float getBpm() {
		return mBpm;
	}
}
