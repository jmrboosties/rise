package fitness.classmate.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ComponentTrack implements Parcelable {

	private SpotifyPlaylistTrack mSpotifyPlaylistTrack;

	public ComponentTrack() { }

	public SpotifyPlaylistTrack getSpotifyPlaylistTrack() {
		return mSpotifyPlaylistTrack;
	}

	public void setSpotifyPlaylistTrack(SpotifyPlaylistTrack spotifyPlaylistTrack) {
		mSpotifyPlaylistTrack = spotifyPlaylistTrack;
	}

	protected ComponentTrack(Parcel in) {
		mSpotifyPlaylistTrack = in.readParcelable(SpotifyPlaylistTrack.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(mSpotifyPlaylistTrack, flags);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ComponentTrack> CREATOR = new Creator<ComponentTrack>() {

		@Override
		public ComponentTrack createFromParcel(Parcel in) {
			return new ComponentTrack(in);
		}

		@Override
		public ComponentTrack[] newArray(int size) {
			return new ComponentTrack[size];
		}
	};

	public long getDuration() {
		//TODO update
		return mSpotifyPlaylistTrack.getDuration();
	}

}
