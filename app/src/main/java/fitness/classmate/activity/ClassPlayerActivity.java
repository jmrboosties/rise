package fitness.classmate.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.spotify.sdk.android.player.Metadata;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.PlayerEvent;
import fitness.classmate.R;
import fitness.classmate.adapter.ClassPlayerAdapter;
import fitness.classmate.base.BasePlayerActivity;
import fitness.classmate.constant.Constants;
import fitness.classmate.model.ClassmateClass;
import fitness.classmate.util.PlayerHelper;
import fitness.classmate.view.PlayerControlsView;
import fitness.classmate.view.PlayerProgressSectionView;

public class ClassPlayerActivity extends BasePlayerActivity {

	private View mPlayerContainer;
	private PlayerControlsView mPlayerControlsView;
	private PlayerProgressSectionView mPlayerProgressSectionView;

	private ClassPlayerAdapter mAdapter;

	private ClassmateClass mClassmateClass;

	@Override
	protected int getMenuResId() {
		return 0;
	}

	@Override
	protected void prepareActivity() {
		mClassmateClass = getIntent().getParcelableExtra(Constants.CLASSMATE_CLASS);
		if(mClassmateClass == null)
			throw new NullPointerException("missing class");

		mAdapter = new ClassPlayerAdapter(this, mClassmateClass);
	}

	@Override
	protected PlayerControlsView getPlayerControlsView() {
		return mPlayerControlsView;
	}

	@Override
	protected PlayerProgressSectionView getPlayerProgressSectionView() {
		return mPlayerProgressSectionView;
	}

	@Override
	protected void initLayout() {
		//Call these first because the super looks for them
		mPlayerControlsView = (PlayerControlsView) findViewById(R.id.player_controls_view);
		mPlayerProgressSectionView = (PlayerProgressSectionView) findViewById(R.id.player_progress_view);

		super.initLayout();

		mPlayerContainer = findViewById(R.id.acp_player_section);

//		ClassComponentView classComponentView = (ClassComponentView) findViewById(R.id.acp_active_component);
//		classComponentView.loadComponent(mClassmateClass.getComponents().get(0));

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.acp_recycler_view);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(linearLayoutManager);

		recyclerView.setAdapter(mAdapter);
	}

	@Override
	protected void onPlayerHelperCreated() {
		mPlayerHelper.setClassmateClass(mClassmateClass);
//		mPlayerHelper.setCurrentTrack(mClassmateClass.getComponents().get(0).getComponentTrack().getSpotifyPlaylistTrack());
//		mPlayerHelper.setProgressSubscriber(mAdapter.getProgressSubscriber());

//		mAdapter.subscribeToPlayerObservable(mPlayerHelper.getObservables());

		mAdapter.subscribeToProgressSubject(mPlayerHelper.getProgressSubject());
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_class_player;
	}

	@Override
	public void onPlaybackEvent(PlayerEvent event, PlaybackState playerState, Metadata metadata) {

	}

	@Override
	protected int getMode() {
		return PlayerHelper.MODE_PLAYLIST;
	}
}
