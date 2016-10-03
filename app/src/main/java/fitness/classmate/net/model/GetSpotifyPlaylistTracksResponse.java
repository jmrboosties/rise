package fitness.classmate.net.model;

import com.google.gson.annotations.SerializedName;
import fitness.classmate.constant.FieldNames;
import fitness.classmate.model.SpotifyPlaylistTrack;

import java.util.ArrayList;

public class GetSpotifyPlaylistTracksResponse {

	@SerializedName(FieldNames.ITEMS)
	private ArrayList<SpotifyPlaylistTrack> mSpotifyTracks;

	@SerializedName(FieldNames.NEXT)
	private String mNextPageUrl;

	public String getNextPageUrl() {
		return mNextPageUrl;
	}

	public ArrayList<SpotifyPlaylistTrack> getSpotifyTracks() {
		return mSpotifyTracks;
	}
}
