package fitness.classmate.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import fitness.classmate.R;
import fitness.classmate.adapter.SpotifyPlaylistAdapter;
import fitness.classmate.base.BaseSpotifyActivity;
import fitness.classmate.constant.Constants;
import fitness.classmate.model.ClassmateClass;
import fitness.classmate.model.SpotifyPlaylist;
import fitness.classmate.net.RetrofitCallback;
import fitness.classmate.net.SpotifyApiHelper;
import fitness.classmate.net.model.GetSpotifyPlaylistsResponse;
import fitness.classmate.preferences.Preferences;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;

public class SpotifyPlaylistActivity extends BaseSpotifyActivity {

	private ClassmateClass mClassmateClass;

	private ArrayList<SpotifyPlaylist> mPlaylists = new ArrayList<>();
	private SpotifyPlaylistAdapter mAdapter;

	@Override
	protected int getMenuResId() {
		return 0;
	}

	@Override
	protected void prepareActivity() {
		super.prepareActivity();
		mClassmateClass = getIntent().getParcelableExtra(Constants.CLASSMATE_CLASS);
		if(mClassmateClass == null)
			throw new NullPointerException("missing classmate class");
	}

	@Override
	protected void initLayout() {
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		mAdapter = new SpotifyPlaylistAdapter(this);
		mAdapter.setOnPlaylistClickedListener(new SpotifyPlaylistAdapter.OnPlaylistClickedListener() {

			@Override
			public void onPlaylistClicked(SpotifyPlaylist playlist) {
				handlePlaylistClick(playlist);
			}

			@Override
			public void onPlaylistLongClicked(SpotifyPlaylist playlist) {
//				handlePlaylistLongClick(playlist);
			}

		});

		recyclerView.setAdapter(mAdapter);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.basic_swipe_refresh_recyclerview;
	}

//	private void handlePlaylistLongClick(SpotifyPlaylist playlist) {
//		//TODO
////		Intent intent = new Intent(this, ClassEditorActivity.class);
////		intent.putExtra(Constants.PLAYLIST_TRACKS_URL, playlist.getTracksUrl());
//
//		startActivity(intent);
//	}

	private void handlePlaylistClick(SpotifyPlaylist playlist) {
		//Send to next activity
		Intent intent = new Intent(this, ClassEditorActivity.class);
		intent.putExtra(Constants.CLASSMATE_CLASS, mClassmateClass);
		intent.putExtra(Constants.SPOTIFY_PLAYLIST, playlist);

		startActivity(intent);
	}

	public void fetchPlaylistsFromSpotify() {
		fetchPlaylistsFromSpotify(null);
	}

	public void fetchPlaylistsFromSpotify(String url) {
		RetrofitCallback.UiCallback<GetSpotifyPlaylistsResponse> listener = new RetrofitCallback.UiCallback<GetSpotifyPlaylistsResponse>() {

			@Override
			public void onResponse(GetSpotifyPlaylistsResponse response) {
				mPlaylists.addAll(response.getSpotifyPlaylists());

				String nextPageUrl = response.getNextPageUrl();
				if(nextPageUrl != null)
					fetchPlaylistsFromSpotify(nextPageUrl);

				loadPlaylistsIntoAdapter();
			}

			@Override
			public void onErrorResponse(Call<GetSpotifyPlaylistsResponse> call, Response<GetSpotifyPlaylistsResponse> response) {
				if(response.code() == 401) {
					Preferences.getInstance().clearSpotifyPrefs();
					connectToSpotifyIfNecessary();
				}
			}

		};

		if(url == null)
			new SpotifyApiHelper(this).getSpotifyPlaylists(listener);
		else
			new SpotifyApiHelper(this).getSpotifyPlaylists(url, listener);
	}

	private void loadPlaylistsIntoAdapter() {
		mAdapter.setSpotifyPlaylists(mPlaylists);
	}

	@Override
	protected void onSpotifyConnected() {
		fetchPlaylistsFromSpotify();
	}

}
