package fitness.classmate.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import fitness.classmate.constant.FieldNames;

import java.util.ArrayList;

public class SpotifyPlaylist implements Parcelable {

	@SerializedName(FieldNames.NAME)
	private String mName;

	@SerializedName(FieldNames.URI)
	private String mUri;

	@SerializedName(FieldNames.SNAPSHOT_ID)
	private String mSnapshotId;

	private int mTrackCount;

	private String mTracksUrl;

	private ArrayList<SpotifyPlaylistTrack> mSpotifyTracks = new ArrayList<>();

	protected SpotifyPlaylist(Parcel in) {
		mName = in.readString();
		mUri = in.readString();
		mSnapshotId = in.readString();
		mTrackCount = in.readInt();
		mTracksUrl = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeString(mUri);
		dest.writeString(mSnapshotId);
		dest.writeInt(mTrackCount);
		dest.writeString(mTracksUrl);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<SpotifyPlaylist> CREATOR = new Creator<SpotifyPlaylist>() {

		@Override
		public SpotifyPlaylist createFromParcel(Parcel in) {
			return new SpotifyPlaylist(in);
		}

		@Override
		public SpotifyPlaylist[] newArray(int size) {
			return new SpotifyPlaylist[size];
		}
	};

	public ArrayList<SpotifyPlaylistTrack> getSpotifyTracks() {
		return mSpotifyTracks;
	}

	public void setSpotifyTracks(ArrayList<SpotifyPlaylistTrack> spotifyTracks) {
		mSpotifyTracks = spotifyTracks;
	}

	public String getName() {
		return mName;
	}

	public String getUri() {
		return mUri;
	}

	public int getTrackCount() {
		return mTrackCount;
	}

	public void setTrackCount(int trackCount) {
		mTrackCount = trackCount;
	}

	public String getTracksUrl() {
		return mTracksUrl;
	}

	public void setTracksUrl(String tracksUrl) {
		mTracksUrl = tracksUrl;
	}

	public String getSnapshotId() {
		return mSnapshotId;
	}

	public void addTracks(ArrayList<SpotifyPlaylistTrack> spotifyTracks) {
		mSpotifyTracks.addAll(spotifyTracks);
	}

	public long getDuration() {
		long duration = 0;
		for(SpotifyPlaylistTrack track : mSpotifyTracks)
			duration += track.getDuration();

		return duration;
	}

	public int getAverageBpm() {
		float sum = 0;
		int countWithAudioFeatures = mTrackCount;
		for(SpotifyPlaylistTrack track : mSpotifyTracks) {
			if(track.getAudioFeatures() != null)
				sum += track.getAudioFeatures().getBpm();
			else
				countWithAudioFeatures--;
		}

		return Math.round(sum / countWithAudioFeatures);
	}

}
