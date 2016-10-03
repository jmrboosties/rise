package fitness.classmate.activity;

import fitness.classmate.R;
import fitness.classmate.base.BaseSpotifyActivity;
import fitness.classmate.constant.Constants;
import fitness.classmate.model.*;
import fitness.classmate.net.RetrofitCallback;
import fitness.classmate.net.SpotifyApiHelper;
import fitness.classmate.net.model.GetSpotifyPlaylistTracksResponse;
import fitness.classmate.util.Print;
import retrofit2.Call;
import retrofit2.Response;

public class ClassPlaylistEditorActivity extends BaseSpotifyActivity {

	private ClassmateClass mClassmateClass;
	private SpotifyPlaylist mSpotifyPlaylist;

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

		mSpotifyPlaylist = getIntent().getParcelableExtra(Constants.SPOTIFY_PLAYLIST);
		if(mSpotifyPlaylist == null)
			throw new NullPointerException("missing playlist");
	}

	private void downloadPlaylistTracks(String url) {
		new SpotifyApiHelper(this).getSpotifyPlaylistTracksWithUrl(url, new RetrofitCallback.UiCallback<GetSpotifyPlaylistTracksResponse>() {

			@Override
			public void onResponse(GetSpotifyPlaylistTracksResponse response) {
				mSpotifyPlaylist.addTracks(response.getSpotifyTracks());

				if(response.getNextPageUrl() != null)
					downloadPlaylistTracks(response.getNextPageUrl());
				else {
					//Attach playlist to class
					attachPlaylistToClass(mSpotifyPlaylist);
					hideProgressDialog();
				}
			}

			@Override
			public void onErrorResponse(Call<GetSpotifyPlaylistTracksResponse> call, Response<GetSpotifyPlaylistTracksResponse> response) {
				if(mSpotifyPlaylist.getSpotifyTracks().size() > 0) {
					//Continue anyway since the error could have happened in next page url
					attachPlaylistToClass(mSpotifyPlaylist);
					hideProgressDialog();
				}
				else {
					//TODO error out
					hideProgressDialog();
				}
			}

		});
	}

	private void attachPlaylistToClass(SpotifyPlaylist playlist) {
		for(int i = 0; i < mClassmateClass.getComponents().size(); i++) {
			//Get playlist item at same index
			SpotifyPlaylistTrack track = playlist.getSpotifyTracks().get(i);

			//Get component
			ClassmateClassComponent component = mClassmateClass.getComponents().get(i);

			//Attach
			ComponentTrack componentTrack = new ComponentTrack();
			componentTrack.setSpotifyPlaylistTrack(track);

			component.setComponentTrack(componentTrack);
		}

		Print.log("attached playlist to class");
	}

	@Override
	protected void onSpotifyConnected() {
		Print.log("spotify is connected");
	}

	@Override
	protected void initLayout() {



		//Start this after the view is initialized
		downloadPlaylistTracks(mSpotifyPlaylist.getTracksUrl());
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_class_playlist_editor;
	}

}
