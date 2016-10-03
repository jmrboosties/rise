package fitness.classmate.base;

import android.content.Intent;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import fitness.classmate.constant.Constants;
import fitness.classmate.net.RetrofitCallback;
import fitness.classmate.net.SpotifyApiHelper;
import fitness.classmate.net.model.SpotifyMe;
import fitness.classmate.preferences.Preferences;
import fitness.classmate.util.Print;
import retrofit2.Call;
import retrofit2.Response;

public abstract class BaseSpotifyActivity extends BaseActivity {

	private static final String REDIRECT_URI = "spinapptest://callback";
	private static final int REQUEST_CODE = 1112;

	@Override
	protected void prepareActivity() {
		connectToSpotifyIfNecessary();
	}

	protected void connectToSpotifyIfNecessary() {
		if(Preferences.getInstance().getSpotifyAccessToken() == null || Preferences.getInstance().getSpotifyUserId() == null) {
			AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(Constants.CLIENT_ID,
					AuthenticationResponse.Type.TOKEN,
					REDIRECT_URI);

			builder.setScopes(new String[]{"user-read-private", "streaming"});
			AuthenticationRequest request = builder.build();

			AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
		}
		else
			onSpotifyConnected();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == REQUEST_CODE) {
			AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);
			if(response.getType() == AuthenticationResponse.Type.TOKEN) {
				Preferences.getInstance().setSpotifyAccessToken(response.getAccessToken());

				new SpotifyApiHelper(this).getSpotifyMe(new RetrofitCallback.UiCallback<SpotifyMe>() {

					@Override
					public void onResponse(SpotifyMe response) {
						Preferences.getInstance().setSpotifyUserId(response.getId());
						onSpotifyConnected();
					}

					@Override
					public void onErrorResponse(Call<SpotifyMe> call, Response<SpotifyMe> response) {
						Print.log("error on spotify me");
					}

				});
			}
		}
	}

	protected abstract void onSpotifyConnected();

}
