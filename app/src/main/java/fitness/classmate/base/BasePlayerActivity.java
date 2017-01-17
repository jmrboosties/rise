package fitness.classmate.base;

import android.support.annotation.CallSuper;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;
import fitness.classmate.constant.Constants;
import fitness.classmate.preferences.Preferences;
import fitness.classmate.util.PlayerHelper;
import fitness.classmate.util.Print;
import fitness.classmate.view.PlayerControlsView;
import fitness.classmate.view.PlayerProgressSectionView;

public abstract class BasePlayerActivity extends BaseActivity implements PlayerHelper.PlayerHelperCallback {

	protected PlayerHelper mPlayerHelper;

	@Override
	@CallSuper
	protected void initLayout() {
		Config playerConfig = new Config(this, Preferences.getInstance().getSpotifyAccessToken(), Constants.CLIENT_ID);
		Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {

			@Override
			public void onInitialized(SpotifyPlayer spotifyPlayer) {
				mPlayerHelper = new PlayerHelper.Builder(spotifyPlayer)
						.setPlayerProgressSectionView(getPlayerProgressSectionView())
						.setPlayerControlsView(getPlayerControlsView())
						.setCallback(BasePlayerActivity.this)
						.setMode(getMode())
						.build();

				onPlayerHelperCreated();
			}

			@Override
			public void onError(Throwable throwable) {
				Print.log("error?");
			}

		});
	}

	protected abstract PlayerControlsView getPlayerControlsView();

	protected abstract PlayerProgressSectionView getPlayerProgressSectionView();

	protected abstract void onPlayerHelperCreated();

	protected int getMode() {
		return PlayerHelper.MODE_SINGLE;
	}

}
