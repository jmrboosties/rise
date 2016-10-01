package fitness.classmate.model;

import com.google.gson.annotations.SerializedName;
import fitness.classmate.constant.FieldNames;

public class SpotifyAudioFeatures {

	@SerializedName(FieldNames.BPM)
	private float mBpm;

	public float getBpm() {
		return mBpm;
	}
}
