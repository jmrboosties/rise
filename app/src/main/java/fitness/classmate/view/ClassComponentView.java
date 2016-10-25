package fitness.classmate.view;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fitness.classmate.R;
import fitness.classmate.model.ClassComponent;
import fitness.classmate.util.Helpbot;
import fitness.classmate.util.Print;

public class ClassComponentView extends RelativeLayout {

	private TextView mTimeLeft;
	private TextView mTitle;
	private TextView mSubtitle;
	private TextView mNotesCount;

	public ClassComponentView(Context context) {
		this(context, null);
	}

	public ClassComponentView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ClassComponentView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.listitem_class_player_component, this, true);

		mTimeLeft = (TextView) findViewById(R.id.lcpc_time_left);
		mTitle = (TextView) findViewById(R.id.lcpc_title);
		mSubtitle = (TextView) findViewById(R.id.lcpc_subtitle);
		mNotesCount = (TextView) findViewById(R.id.lcpc_notes_count);

		setBackground(ContextCompat.getDrawable(getContext(), R.drawable.clickable_listitem));

		int padding = (int) getResources().getDimension(R.dimen.list_item_padding);
		setPadding(padding, padding, padding, padding);

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			float elevation = getResources().getDimension(R.dimen.list_item_elevation);
			setElevation(elevation);
		}

		Print.log("layout param height", getLayoutParams() != null ? getLayoutParams().height : "layout params are null");
	}

	public void setTimeLeft(String timeLeft) {
		mTimeLeft.setText(timeLeft);
	}

	public void setTitle(String title) {
		mTitle.setText(title);
	}

	public void setSubtitle(String subtitle) {
		mSubtitle.setText(subtitle);
	}

	public void setNotesCount(int count) {
		mNotesCount.setText(getResources().getString(R.string.notes_count, count));
	}

	public void loadComponent(ClassComponent component) {
		setTitle(component.getName());
		setSubtitle(component.getComponentTrack().getSpotifyPlaylistTrack().getName());
		setTimeLeft(Helpbot.getDurationTimestampFromMillis(component.getDuration()));
		setNotesCount(component.getComponentNotes().size());
	}

}
