package fitness.classmate.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import fitness.classmate.R;
import fitness.classmate.adapter.ClassPlayerNoteAdapter;
import fitness.classmate.base.BaseSpotifyActivity;
import fitness.classmate.constant.Constants;
import fitness.classmate.model.ClassmateClass;
import fitness.classmate.view.ClassComponentView;

public class ClassPlayerActivity extends BaseSpotifyActivity {

	private View mPlayerContainer;

	private ClassmateClass mClassmateClass;

	@Override
	protected void onSpotifyConnected() {
		//TODO rx to say spotify ready
	}

	@Override
	protected int getMenuResId() {
		return 0;
	}

	@Override
	protected void prepareActivity() {
		super.prepareActivity();

		mClassmateClass = getIntent().getParcelableExtra(Constants.CLASSMATE_CLASS);
		if(mClassmateClass == null)
			throw new NullPointerException("missing class");
	}

	@Override
	protected void initLayout() {
		mPlayerContainer = findViewById(R.id.acp_player_section);
		//TODO rx to say player inflated

		ClassComponentView classComponentView = (ClassComponentView) findViewById(R.id.acp_active_component);
		classComponentView.loadComponent(mClassmateClass.getComponents().get(0));

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.acp_recycler_view);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(linearLayoutManager);

		ClassPlayerNoteAdapter adapter = new ClassPlayerNoteAdapter(this);
		recyclerView.setAdapter(adapter);

		adapter.setNotes(mClassmateClass.getComponents().get(0).getComponentNotes());
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_class_player;
	}

}
