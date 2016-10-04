package fitness.classmate.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import fitness.classmate.R;

public class PlayerControlsView extends RelativeLayout {

	private ImageView mPlayPauseButton;
	private ImageView mBack;
	private ImageView mNext;

	private boolean mPlayIconDisplayed = true;

	private ControlsCallback mControlsCallback;

	public PlayerControlsView(Context context) {
		this(context, null);
	}

	public PlayerControlsView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PlayerControlsView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.widget_player_controls_section, this, true);

		mPlayPauseButton = (ImageView) findViewById(R.id.pcs_play_pause);
		mBack = (ImageView) findViewById(R.id.pcs_back);
		mNext = (ImageView) findViewById(R.id.pcs_next);

		//TODO press and hold to pause for all of these buttons
		mPlayPauseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mControlsCallback != null)
					mControlsCallback.onPlayPauseClicked();
			}

		});

		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mControlsCallback != null)
					mControlsCallback.onBack();
			}

		});

		mNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mControlsCallback != null)
					mControlsCallback.onNext();
			}

		});
	}

	public void setControlsCallback(ControlsCallback controlsCallback) {
		mControlsCallback = controlsCallback;
	}

	public ImageView getPlayPauseButton() {
		return mPlayPauseButton;
	}

	public ImageView getBackButton() {
		return mBack;
	}

	public ImageView getNextButton() {
		return mNext;
	}

	public void togglePlayPauseIcon() {
		if(mPlayIconDisplayed)
			showPauseIcon();
		else
			showPlayIcon();
	}

	public void showPlayIcon() {
		mPlayPauseButton.setImageResource(android.R.drawable.ic_media_play);
		mPlayIconDisplayed = true;
	}

	public void showPauseIcon() {
		mPlayPauseButton.setImageResource(android.R.drawable.ic_media_pause);
		mPlayIconDisplayed = false;
	}

	public interface ControlsCallback {

		void onPlayPauseClicked();

		void onBack();

		void onNext();

	}

}
