package fitness.classmate.net;

import android.content.Context;
import fitness.classmate.net.api.SpotifyApi;
import fitness.classmate.net.gson.GsonFactory;
import fitness.classmate.net.model.GetSpotifyPlaylistsResponse;
import fitness.classmate.preferences.Preferences;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static fitness.classmate.constant.Constants.DEFAULT_TIMEOUT;

public class SpotifyApiHelper {

	private Context mContext;
	private String mUserId;
	private String mAccessToken;

	private static final String SPOTIFY_AUTH_HEADER_KEY = "Authorization";
	private static final String SPOTIFY_AUTHORIZATION_VALUE_FORMAT = "Bearer %s";
	private static final String BASE_URL = "https://api.spotify.com/v1/";

	private static SpotifyApi spotifyApi;

	private static SpotifyApi getSpotifyApiInstance(String accessToken) {
		if(spotifyApi == null)
			spotifyApi = createRetrofit(buildClient(accessToken)).create(SpotifyApi.class);

		return spotifyApi;
	}

	public SpotifyApiHelper(Context context) {
		mContext = context;
		mUserId = Preferences.getInstance().getSpotifyUserId();
		mAccessToken = Preferences.getInstance().getSpotifyAccessToken();
	}

	private static Retrofit createRetrofit(OkHttpClient client) {
		return new Retrofit.Builder()
				.client(client)
				.baseUrl(BASE_URL)
				.addConverterFactory(GsonConverterFactory.create(GsonFactory.createGson()))
				.build();
	}

	private static OkHttpClient buildClient(final String accessToken) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder()
				.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
				.addInterceptor(new Interceptor() {

					@Override
					public Response intercept(Chain chain) throws IOException {
						Request original = chain.request();
						Request.Builder requestBuilder = original.newBuilder();

						//Add access token header to all requests
						requestBuilder.addHeader(SPOTIFY_AUTH_HEADER_KEY, String.format(SPOTIFY_AUTHORIZATION_VALUE_FORMAT, accessToken));

						Request request = requestBuilder.build();
						return chain.proceed(request);
					}
				});

		return builder.build();
	}

	private SpotifyApi getApi() {
		return getSpotifyApiInstance(mAccessToken);
	}

	public void getSpotifyPlaylists(RetrofitCallback.UiCallback<GetSpotifyPlaylistsResponse> uiCallback) {
		Call<GetSpotifyPlaylistsResponse> call = getApi().getPlaylists(mUserId, 50);
		call.enqueue(new RetrofitCallback<>(uiCallback));
	}

}
