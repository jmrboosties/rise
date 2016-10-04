package fitness.classmate.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.jakewharton.rxbinding.view.RxView;
import fitness.classmate.R;
import fitness.classmate.adapter.ClassPlaylistEditorAdapter;
import fitness.classmate.adapter.ClassPlaylistEditorGraphAdapter;
import fitness.classmate.base.BaseActivity;
import fitness.classmate.decorator.ComponentDecorator;
import fitness.classmate.model.ClassmateClassComponent;
import fitness.classmate.util.Print;
import fitness.classmate.view.ClassGraphLayoutManager;
import rx.Observable;
import rx.functions.Func2;

import java.util.ArrayList;

public class TestDrag extends BaseActivity {

	private RecyclerView mGraph;
	private RecyclerView mPlaylist;

	private ComponentDecorator mComponentDecorator;

	@Override
	protected int getMenuResId() {
		return 0;
	}

	@Override
	protected void prepareActivity() {

	}

	@Override
	protected void initLayout() {
		mGraph = (RecyclerView) findViewById(R.id.acpe_class_graph);
	    mPlaylist = (RecyclerView) findViewById(R.id.acpe_playlist);

	    ClassGraphLayoutManager graphManager = new ClassGraphLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
	    ClassGraphLayoutManager playlistManage = new ClassGraphLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

		mGraph.setLayoutManager(graphManager);
		mPlaylist.setLayoutManager(playlistManage);

	    mComponentDecorator = new ComponentDecorator(this);

		mGraph.addItemDecoration(mComponentDecorator);
		mPlaylist.addItemDecoration(mComponentDecorator);

		mGraph.setOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);

				mPlaylist.scrollBy(dx, dy);
			}

		});

		mPlaylist.setOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);

				Print.log("might luck out on this one", dx, dy);
			}

		});

//		mGraph.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//			@Override
//			public void onGlobalLayout() {
//				buildGraphAdapter();
//				mGraph.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//			}
//		});

	    final Observable graphObservable = RxView.globalLayouts(mGraph);
		final Observable playlistObservable = RxView.globalLayouts(mPlaylist);

		Observable.zip(graphObservable, playlistObservable, new Func2() {

			@Override
			public Object call(Object o, Object o2) {
				Print.log("should only see this once");

				int barWidth = getBarWidth();
				buildGraphAdapter(barWidth);
				buildPlaylistAdapter(barWidth);

				return null;
			}

		}).first().subscribe();

    }

	private int getBarWidth() {
		final int verticalPadding = mPlaylist.getPaddingTop() + mPlaylist.getPaddingBottom();
		final int spacing = mComponentDecorator.getSpacing();

		int poolHeight = mPlaylist.getHeight();
		int minusPaddingAndSpacing = poolHeight - verticalPadding - (spacing * 2);

		Print.log("final value", minusPaddingAndSpacing);
		return minusPaddingAndSpacing;
	}

	private void buildGraphAdapter(int barWidth) {
		ClassPlaylistEditorGraphAdapter graphAdapter = new ClassPlaylistEditorGraphAdapter(TestDrag.this, mGraph.getHeight(), barWidth, mComponentDecorator.getSpacing());
		mGraph.setAdapter(graphAdapter);

		//TODO fake items
		ArrayList<String> componentStrings = new ArrayList<>();
		componentStrings.add("Apple");
		componentStrings.add("Carrot");
		componentStrings.add("Dog");
		componentStrings.add("EightCha");
		componentStrings.add("Rats");
		componentStrings.add("Apple");
		componentStrings.add("Carrot");
		componentStrings.add("Dog");
		componentStrings.add("EightCha");
		componentStrings.add("Rats");
		componentStrings.add("Apple");
		componentStrings.add("Carrot");
		componentStrings.add("Dog");
		componentStrings.add("EightCha");
		componentStrings.add("Rats");

		ArrayList<ClassmateClassComponent> components = new ArrayList<>();
		for(String s : componentStrings)
			components.add(buildComponentFromString(s));

		graphAdapter.setComponents(components);

	}

	//TODO remove this
	private ClassmateClassComponent buildComponentFromString(String s) {
		ClassmateClassComponent classmateClassComponent = new ClassmateClassComponent();
		classmateClassComponent.setName(s);

		float f = s.length() / 8f;
		int intensity = Math.round(f * 4);

		classmateClassComponent.setIntensity(intensity);

		return classmateClassComponent;
	}

	private void buildPlaylistAdapter(int barWidth) {
		ArrayList<String> componentStrings = new ArrayList<>();
		componentStrings.add("Apple");
		componentStrings.add("Carrot");
		componentStrings.add("Dog");
		componentStrings.add("EightCha");
		componentStrings.add("Rats");
		componentStrings.add("Apple");
		componentStrings.add("Carrot");
		componentStrings.add("Dog");
		componentStrings.add("EightCha");
		componentStrings.add("Rats");
		componentStrings.add("Apple");
		componentStrings.add("Carrot");
		componentStrings.add("Dog");
		componentStrings.add("EightCha");
		componentStrings.add("Rats");

		ClassPlaylistEditorAdapter editorAdapter = new ClassPlaylistEditorAdapter(this, barWidth);
		editorAdapter.setComponents(componentStrings);

		mPlaylist.setAdapter(editorAdapter);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_class_playlist_editor;
	}

}
