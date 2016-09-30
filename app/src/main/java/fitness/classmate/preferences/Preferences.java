package fitness.classmate.preferences;

import android.content.Context;

public class Preferences extends AbsPreferences {

	private static Preferences instance;

	private static final String SPOTIFY_ACCESS_TOKEN = "spotify_access_token";
	private static final String SPOTIFY_USER_ID = "spotify_user_id";

	public static void initialize(Context mContext) {
		instance = new Preferences(mContext);
	}

	public static Preferences getInstance() {
		if(instance == null)
			throw new NullPointerException("instance does not exist");

		return instance;
	}

	protected Preferences(Context context) {
		super(context);
	}

	public void setSpotifyAccessToken(String accessToken) {
		setPref(SPOTIFY_ACCESS_TOKEN, accessToken);
	}

	public String getSpotifyAccessToken() {
		return getStringPref(SPOTIFY_ACCESS_TOKEN);
	}

	public void setSpotifyUserId(String userId) {
		setPref(SPOTIFY_USER_ID, userId);
	}

	public String getSpotifyUserId() {
		return getStringPref(SPOTIFY_USER_ID);
	}

	public void clearSpotifyPrefs() {
		clearPref(SPOTIFY_ACCESS_TOKEN);
		clearPref(SPOTIFY_USER_ID);
	}


}
