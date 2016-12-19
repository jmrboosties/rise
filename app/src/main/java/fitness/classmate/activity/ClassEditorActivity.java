package fitness.classmate.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.spotify.sdk.android.player.*;
import fitness.classmate.R;
import fitness.classmate.adapter.ClassmateNoteEditorAdapter;
import fitness.classmate.base.BaseActivity;
import fitness.classmate.constant.Constants;
import fitness.classmate.dialog.NewMoveDialogBuilder;
import fitness.classmate.model.*;
import fitness.classmate.net.RetrofitCallback;
import fitness.classmate.net.SpotifyApiHelper;
import fitness.classmate.net.model.GetSpotifyPlaylistTracksResponse;
import fitness.classmate.preferences.Preferences;
import fitness.classmate.util.Helpbot;
import fitness.classmate.util.PlayerHelper;
import fitness.classmate.util.Print;
import fitness.classmate.view.PlayerControlsView;
import fitness.classmate.view.PlayerProgressSectionView;
import retrofit2.Call;
import retrofit2.Response;


public class ClassEditorActivity extends BaseActivity implements PlayerHelper.PlayerHelperCallback {

	private PlayerProgressSectionView mPlayerProgressSectionView;
	private PlayerControlsView mPlayerControlsView;

	private PlayerHelper mPlayerHelper;

	private ClassmateClass mClassmateClass;
	private SpotifyPlaylist mSpotifyPlaylist;

	private ClassmateNoteEditorAdapter mAdapter;

	@Override
	protected void prepareActivity() {
		mClassmateClass = getIntent().getParcelableExtra(Constants.CLASSMATE_CLASS);
		if(mClassmateClass == null)
			throw new NullPointerException("missing classmate class");

		mSpotifyPlaylist = getIntent().getParcelableExtra(Constants.SPOTIFY_PLAYLIST);
		if(mSpotifyPlaylist == null)
			throw new NullPointerException("missing playlist");

		Config playerConfig = new Config(this, Preferences.getInstance().getSpotifyAccessToken(), Constants.CLIENT_ID);
		Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {

			@Override
			public void onInitialized(SpotifyPlayer spotifyPlayer) {
				mPlayerHelper = new PlayerHelper.Builder(spotifyPlayer)
						.setPlayerProgressSectionView(mPlayerProgressSectionView)
						.setPlayerControlsView(mPlayerControlsView)
						.setCallback(ClassEditorActivity.this)
						.build();
			}

			@Override
			public void onError(Throwable throwable) {

			}

		});
	}

	@Override
	protected void initLayout() {
		mPlayerControlsView = (PlayerControlsView) findViewById(R.id.ace_controls_view);
		mPlayerProgressSectionView = (PlayerProgressSectionView) findViewById(R.id.ace_progress_view);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tracks_list);

		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

			@Override
			public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
				return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
			}

			@Override
			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
				mAdapter.swapItems(viewHolder.getAdapterPosition(), target.getAdapterPosition());
				return true;
			}

			@Override
			public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

			}

			@Override
			public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
				super.clearView(recyclerView, viewHolder);
				mAdapter.onSwapComplete();
			}

			@Override
			public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
				super.onSelectedChanged(viewHolder, actionState);
				Print.log("on selected changed");
			}

		});

		itemTouchHelper.attachToRecyclerView(recyclerView);

		mAdapter = new ClassmateNoteEditorAdapter(this);
		mAdapter.setAdapterClickListener(new ClassmateNoteEditorAdapter.AdapterClickListener() {

			@Override
			public void onNewNoteClicked(ClassComponent component) {
				openNewMoveDialog();
			}

			@Override
			public void onComponentSelected(ClassComponent component) {
				mPlayerHelper.setCurrentTrack(component.getComponentTrack().getSpotifyPlaylistTrack());
				displayComponentNotes();
			}

		});

		recyclerView.setAdapter(mAdapter);

		downloadPlaylistTracks(mSpotifyPlaylist.getTracksUrl());

		ImageView newButton = (ImageView) findViewById(R.id.add);

//		final PopupMenu popupMenu = new PopupMenu(this, newButton);
//		popupMenu.inflate(R.menu.new_class_note);

		newButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//TODO launch full screen chooser when there are multiple options for types
				openNewMoveDialog();
			}

		});

//		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//			@Override
//			public boolean onMenuItemClick(MenuItem item) {
//				mPlayerHelper.pause();
//
//				switch(item.getItemId()) {
//					case R.id.new_move :
//						openNewMoveDialog();
//						return true;
//					case R.id.new_fadeout :
//
//						return true;
//					default :
//						return false;
//				}
//			}
//		});
	}

	private void displayComponentNotes() {
		//Clear any existing notes
		mPlayerProgressSectionView.clearNoteViews();

		ClassComponent component = mAdapter.getSelectedComponent();
		if(component != null) {
			for(ComponentNote note : component.getComponentNotes())
				addClassComponentNote(note, component.getComponentTrack().getSpotifyPlaylistTrack());
		}
	}

	public void addClassComponentNote(final ComponentNote note, SpotifyPlaylistTrack track) {
		float progressAsPercentage = (float) note.getTimestamp() / (float) track.getDuration();
		Print.log("progress as percentage for classnote", progressAsPercentage);

		mPlayerProgressSectionView.addClassNoteViewToFrameLayout(progressAsPercentage, new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//TODO
				Print.log("class note clicked", note.getMessage());
			}

		});
	}

	private void downloadPlaylistTracks(String url) {
		new SpotifyApiHelper(this).getSpotifyPlaylistTracksWithUrl(url, new RetrofitCallback.UiCallback<GetSpotifyPlaylistTracksResponse>() {

			@Override
			public void onResponse(GetSpotifyPlaylistTracksResponse response) {
				mSpotifyPlaylist.addTracks(response.getSpotifyTracks());

//				getAudioFeaturesForTracks();

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
			ClassComponent component = mClassmateClass.getComponents().get(i);

			//Attach
			ComponentTrack componentTrack = new ComponentTrack();
			componentTrack.setSpotifyPlaylistTrack(track);

			component.setComponentTrack(componentTrack);
		}

		getAudioFeaturesForClassTracks(mClassmateClass);

		loadClassIntoAdapter();
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_class_editor;
	}

	private void openNewMoveDialog() {
		mPlayerHelper.pause();

		PlaybackState playbackState = mPlayerHelper.getPlaybackState();
		String currentTime = Helpbot.getDurationTimestampFromMillis(playbackState.positionMs);

		NewMoveDialogBuilder builder = new NewMoveDialogBuilder(ClassEditorActivity.this, currentTime, new NewMoveDialogBuilder.OnConfirmClickListener() {

			@Override
			public void onConfirmClicked(String time, String description) {
				ComponentNote note = new ComponentNote();
				note.setMessage(description);
				note.setTimestamp(Helpbot.getMillisFromTimestamp(time));

				mAdapter.getSelectedComponent().addComponentNote(note);

				mAdapter.reloadNotes();

				//TODO pretty sure below line can go
//				mAdapter.notifyItemChanged(mSpotifyPlaylist.getSpotifyTracks().indexOf(mPlayerHelper.getCurrentTrack()));

				addClassComponentNote(note, mPlayerHelper.getCurrentTrack());

				//Resume
				mPlayerHelper.resume(); //TODO issue here?
			}

		});

		showDialog(builder.build());
	}

	private void getAudioFeaturesForClassTracks(ClassmateClass classmateClass) {
		SpotifyApiHelper apiHelper = new SpotifyApiHelper(this);
		for(final ClassComponent component : classmateClass.getComponents()) {
			final SpotifyPlaylistTrack track = component.getComponentTrack().getSpotifyPlaylistTrack();

			apiHelper.getTrackAudioFeatures(track.getUri().split(":")[2], new RetrofitCallback.UiCallback<SpotifyAudioFeatures>() {

				@Override
				public void onResponse(SpotifyAudioFeatures response) {
					track.setAudioFeatures(response);
					mAdapter.itemChanged(component);
				}

				@Override
				public void onErrorResponse(Call<SpotifyAudioFeatures> call, Response<SpotifyAudioFeatures> response) {
					Print.log("error getting audio information for track", track.getName());
				}

			});
		}
	}

	private void loadClassIntoAdapter() {
		mAdapter.setClassmateClass(mClassmateClass);
	}


	@Override
	protected int getMenuResId() {
		return R.menu.menu_done;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.ccb_done)
			onDone();

		return super.onOptionsItemSelected(item);
	}

	private void onDone() {
		//TODO update this to have correct behavior
		//Go to player just for now
		Intent intent = new Intent(this, ClassPlayerActivity.class);
		intent.putExtra(Constants.CLASSMATE_CLASS, mClassmateClass);

		mPlayerHelper.pause();

		startActivity(intent);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mPlayerHelper.pause();
	}

	@Override
	public void onDestroy() {
		Spotify.destroyPlayer(this);
		super.onDestroy();
	}

	@Override
	public void onPlaybackEvent(PlayerEvent event, PlaybackState playerState, Metadata metadata) {

	}

}
