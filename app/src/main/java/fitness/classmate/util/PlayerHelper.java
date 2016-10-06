package fitness.classmate.util;

import android.os.AsyncTask;
import android.widget.SeekBar;
import com.spotify.sdk.android.player.*;
import com.spotify.sdk.android.player.Error;
import fitness.classmate.model.SpotifyPlaylist;
import fitness.classmate.model.SpotifyPlaylistTrack;
import fitness.classmate.view.PlayerControlsView;
import fitness.classmate.view.PlayerProgressSectionView;

/**
 * Does a bunch of shit related to the player so you only have to deal with it once.
 */
public class PlayerHelper implements ConnectionStateCallback, PlayerControlsView.ControlsCallback, Player.NotificationCallback {

	private SpotifyPlayer mPlayer;
	private PlayerProgressSectionView mPlayerProgressSectionView;
	private PlayerHelperCallback mPlayerHelperCallback;
	private PlayerProgressHandler mPlayerProgressHandler;
	private PlayerControlsView mPlayerControlsView;

	private SpotifyPlaylist mSpotifyPlaylist;
	private SpotifyPlaylistTrack mCurrentTrack;

	private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {

		@Override
		public void onSuccess() {
			Print.log("success");
		}

		@Override
		public void onError(Error error) {
			Print.log("failure");
		}

	};

	private void setPlayer(SpotifyPlayer player) {
		mPlayer = player;

		mPlayer.addConnectionStateCallback(this);
		mPlayer.addNotificationCallback(this);
	}

	private void setPlayerProgressSectionView(PlayerProgressSectionView playerProgressSectionView) {
		mPlayerProgressSectionView = playerProgressSectionView;
	}

	private void setPlayerHelperCallback(PlayerHelperCallback playerHelperCallback) {
		mPlayerHelperCallback = playerHelperCallback;
	}

	private void setPlayerControlsView(PlayerControlsView playerControlsView) {
		mPlayerControlsView = playerControlsView;

		mPlayerControlsView.setControlsCallback(this);
	}

	private void setSpotifyPlaylist(SpotifyPlaylist playlist) {
		mSpotifyPlaylist = playlist;
	}

	private void build() {
		//Set up seekbar to respond to moving it around
		mPlayerProgressSectionView.getSeekBar().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(fromUser)
					mPlayerProgressSectionView.getTimeProgress().setText(Helpbot.getDurationTimestampFromMillis(progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				Print.log("on start tracking touch");
				//Stop the seekbar from updating from player
				cancelPlayerProgressHandler();
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Print.log("on stop tracking touch");
				int progress = seekBar.getProgress();

				//Set player to start from here
				mPlayer.seekToPosition(mOperationCallback, progress);
			}

		});
	}

	public PlaybackState getPlaybackState() {
		return mPlayer.getPlaybackState();
	}

	public Metadata getMetadata() {
		return mPlayer.getMetadata();
	}

	@Override
	public void onLoggedIn() {

	}

	@Override
	public void onLoggedOut() {

	}

	@Override
	public void onLoginFailed(int i) {

	}

	@Override
	public void onTemporaryError() {

	}

	@Override
	public void onConnectionMessage(String s) {

	}

	public void setCurrentTrack(SpotifyPlaylistTrack track) {
		//If its the same do nothing
		if(track != mCurrentTrack) {
			mCurrentTrack = track;
			pause();
		}
	}

	public void playTrack(final SpotifyPlaylistTrack track) {
		PlaybackState state = mPlayer.getPlaybackState();
		Metadata metadata = mPlayer.getMetadata();

		if(metadata != null && metadata.currentTrack != null && track.getUri().equals(metadata.currentTrack.uri)) {
			Print.log("track has same uri");

			if(!state.isPlaying) {
				Print.log("resuming track");
				mPlayer.resume(mOperationCallback);
			}
			else
				Print.log("track already playing");
		}
		else {
			Print.log("existing uri differs, starting over");

			mCurrentTrack = track;
			mPlayer.playUri(mOperationCallback, track.getUri(), 0, 0);
		}

		mPlayerControlsView.showPauseIcon();
	}

//	@Override
//	public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
//		Print.log("playerstate", playerState);
//		Print.log("eventtype", eventType);
//
//		mPlayerProgressSectionView.getSeekBar().setProgress(playerState.positionInMs);
//		mPlayerProgressSectionView.getSeekBar().setMax(playerState.durationInMs);
//
//		mPlayerProgressSectionView.getTimeProgress().setText(Helpbot.getDurationTimestampFromMillis(playerState.positionInMs));
//		mPlayerProgressSectionView.getDuration().setText(Helpbot.getDurationTimestampFromMillis(playerState.durationInMs));
//
//		if(mCurrentTrack == null || eventType == EventType.TRACK_CHANGED) {
//			mCurrentTrack = getTrackFromUri(playerState.trackUri);
//			displayClassNotes();
//		}
//
//		if(eventType == EventType.PLAY || playerState.playing) {
//			Print.log("starting progress handler");
//
//			generateNewPlayerProgressHandler();
//			mPlayerProgressHandler.execute();
//		}
//		else if(eventType == EventType.LOST_PERMISSION) {
//			//TODO toast saying they lost a permission probably due to playing on another device
//		}
//
//		if(mPlayerHelperCallback != null)
//			mPlayerHelperCallback.onPlaybackEvent(eventType, playerState);
//	}

	private SpotifyPlaylistTrack getTrackFromUri(String uri) {
		if(mSpotifyPlaylist != null) {
			for(SpotifyPlaylistTrack track : mSpotifyPlaylist.getSpotifyTracks()) {
				if(track.getUri().equals(uri))
					return track;
			}

			return null;
		}
		else if(mCurrentTrack != null)
			return mCurrentTrack;
		else
			return null;
	}

	@Override
	public void onPlayPauseClicked() {
		PlaybackState state = mPlayer.getPlaybackState();
		Metadata metadata = mPlayer.getMetadata();

		if(mCurrentTrack == null) {
			//TODO something?
			return;
		}

		if(metadata == null || metadata.currentTrack == null || !mCurrentTrack.getUri().equals(metadata.currentTrack.uri))
			playTrack(mCurrentTrack);
		else if(metadata.currentTrack.uri != null) {
			if(state.isPlaying)
				pause();
			else
				resume();
		}
		else {
			//TODO pick first off list and beign playing it
		}
	}

	@Override
	public void onBack() {

	}

	@Override
	public void onNext() {

	}

	public void onDestroy() {
		cancelPlayerProgressHandler();
	}

	public void pause() {
		mPlayer.pause(mOperationCallback);
		mPlayerControlsView.showPlayIcon();
	}

	public void resume() {
		mPlayer.resume(mOperationCallback);
		mPlayerControlsView.showPauseIcon();
	}

	@Override
	public void onPlaybackEvent(PlayerEvent playerEvent) {
		Print.log("player event", playerEvent);

		PlaybackState state = mPlayer.getPlaybackState();
		Metadata metadata = mPlayer.getMetadata();

		if(mCurrentTrack == null || playerEvent == PlayerEvent.kSpPlaybackNotifyTrackChanged) {
			setCurrentTrack(getTrackFromUri(metadata.currentTrack.uri));
		}

		if(playerEvent == PlayerEvent.kSpPlaybackNotifyLostPermission) {
			//TODO toast saying they lost a permission probably due to playing on another device
			return;
		}
		else if(playerEvent == PlayerEvent.kSpPlaybackNotifyPlay || state.isPlaying) {
			mPlayerProgressSectionView.getSeekBar().setProgress((int) state.positionMs);
			mPlayerProgressSectionView.getSeekBar().setMax((int) metadata.currentTrack.durationMs);

			mPlayerProgressSectionView.getTimeProgress().setText(Helpbot.getDurationTimestampFromMillis(state.positionMs));
			mPlayerProgressSectionView.getDuration().setText(Helpbot.getDurationTimestampFromMillis(metadata.currentTrack.durationMs));

//			if(mCurrentTrack == null || playerEvent == PlayerEvent.kSpPlaybackNotifyTrackChanged) {
//				mCurrentTrack = getTrackFromUri(metadata.currentTrack.uri);
//				displayClassNotes();
//			}

//			if(playerEvent == PlayerEvent.kSpPlaybackNotifyPlay || state.isPlaying) {
				Print.log("starting progress handler");

				generateNewPlayerProgressHandler();
				mPlayerProgressHandler.execute();
//			}
		}

		if(mPlayerHelperCallback != null)
			mPlayerHelperCallback.onPlaybackEvent(playerEvent, state, metadata);
	}

	@Override
	public void onPlaybackError(Error error) {

	}

	private class PlayerProgressHandler extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			while(!isCancelled()) {
				try {
					Thread.sleep(200L);
				} catch(InterruptedException e) {
//					Don't report, no need
				}

				publishProgress();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Void... v) {
			if(!mPlayer.isShutdown()) {
				PlaybackState state = mPlayer.getPlaybackState();

				if(state.isPlaying) {
					mPlayerProgressSectionView.getSeekBar().setProgress((int) state.positionMs);
					mPlayerProgressSectionView.getTimeProgress().setText(Helpbot.getDurationTimestampFromMillis(state.positionMs));
				}
				else
					cancelPlayerProgressHandler();
			}
			else
				cancelPlayerProgressHandler();
		}

	}

	private void generateNewPlayerProgressHandler() {
		cancelPlayerProgressHandler();

		mPlayerProgressHandler = new PlayerProgressHandler();
	}

	private void cancelPlayerProgressHandler() {
		Print.log("got cancelled!");
		if(mPlayerProgressHandler != null)
			mPlayerProgressHandler.cancel(true);

		mPlayerProgressHandler = null;
	}

//	public void addClassComponentNote(final ClassNote classNote, SpotifyPlaylistTrack track) {
//		float progressAsPercentage = (float) classNote.getTimestamp() / (float) track.getDuration();
//		Print.log("progress as percentage for classnote", progressAsPercentage);
//
//		mPlayerProgressSectionView.addClassNoteViewToFrameLayout(progressAsPercentage, new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Print.log("class note clicked", classNote.getType() + ": " + classNote.getDescription());
//			}
//
//		});
//	}

	public SpotifyPlaylistTrack getCurrentTrack() {
		return mCurrentTrack;
	}

	public SpotifyPlaylist getSpotifyPlaylist() {
		return mSpotifyPlaylist;
	}

	public interface PlayerHelperCallback {

		void onPlaybackEvent(PlayerEvent event, PlaybackState playerState, Metadata metadata);

	}

	public static class Builder {

		private PlayerHelper mPlayerHelper;

		public Builder(SpotifyPlayer player) {
			mPlayerHelper = new PlayerHelper();
			mPlayerHelper.setPlayer(player);
		}

		public Builder setPlayerProgressSectionView(PlayerProgressSectionView playerProgressSectionView) {
			mPlayerHelper.setPlayerProgressSectionView(playerProgressSectionView);
			return this;
		}

		public Builder setCallback(PlayerHelperCallback callback) {
			mPlayerHelper.setPlayerHelperCallback(callback);
			return this;
		}

		public Builder setPlayerControlsView(PlayerControlsView playerControlsView) {
			mPlayerHelper.setPlayerControlsView(playerControlsView);
			return this;
		}

		public Builder setSpotifyPlaylist(SpotifyPlaylist spotifyPlaylist) {
			mPlayerHelper.setSpotifyPlaylist(spotifyPlaylist);
			return this;
		}

		public Builder setSpotifyTrack(SpotifyPlaylistTrack track) {
			mPlayerHelper.mCurrentTrack = track;
			return this;
		}

		public PlayerHelper build() {
			//TODO check to make sure all necessary components are present
			mPlayerHelper.build();

//			if(mPlayerHelper.mCurrentTrack == null && mPlayerHelper.mSpotifyPlaylist == null)
//				throw new NullPointerException("missing track and playlist, provide one");
//			else if(mPlayerHelper.mCurrentTrack != null && mPlayerHelper.mSpotifyPlaylist != null)
//				throw new IllegalArgumentException("don't provide both track and playlist, only one");

			return mPlayerHelper;
		}

	}

}
