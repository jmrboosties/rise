package fitness.classmate.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import com.jakewharton.rxbinding.view.RxView;
import fitness.classmate.R;
import fitness.classmate.adapter.ClassPlaylistEditorAdapter;
import fitness.classmate.base.BaseSpotifyActivity;
import fitness.classmate.decorator.ComponentDecorator;
import fitness.classmate.model.*;
import fitness.classmate.net.RetrofitCallback;
import fitness.classmate.net.SpotifyApiHelper;
import fitness.classmate.net.model.GetSpotifyPlaylistTracksResponse;
import fitness.classmate.util.Print;
import fitness.classmate.view.ClassGraphLayoutManager;
import retrofit2.Call;
import retrofit2.Response;
import rx.functions.Action1;

import java.util.ArrayList;

public class ClassPlaylistEditorActivity extends BaseSpotifyActivity {

	private RecyclerView mGraph;
	private FrameLayout mMask;

	private ComponentDecorator mComponentDecorator;

	private ClassPlaylistEditorAdapter mAdapter;

	private ClassmateClass mClassmateClass;
	private SpotifyPlaylist mSpotifyPlaylist;

	private int mCurrentClassDx;

	@Override
	protected int getMenuResId() {
		return 0;
	}

	@Override
	protected void prepareActivity() {
		super.prepareActivity();
//		mClassmateClass = getIntent().getParcelableExtra(Constants.CLASSMATE_CLASS);
//		if(mClassmateClass == null)
//			throw new NullPointerException("missing classmate class");
//
//		mSpotifyPlaylist = getIntent().getParcelableExtra(Constants.SPOTIFY_PLAYLIST);
//		if(mSpotifyPlaylist == null)
//			throw new NullPointerException("missing playlist");
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
		mGraph = (RecyclerView) findViewById(R.id.acpe_class_graph);

		ClassGraphLayoutManager graphManager = new ClassGraphLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		mGraph.setLayoutManager(graphManager);

		mComponentDecorator = new ComponentDecorator(this);

		mGraph.addItemDecoration(mComponentDecorator);

		mGraph.setOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);

				mCurrentClassDx += dx;
				mCurrentClassDx = Math.max(0, mCurrentClassDx);
				mCurrentClassDx = Math.min(mCurrentClassDx, mAdapter.getEstimatedFullWidth());
			}

		});

		RxView.globalLayouts(mGraph).doOnNext(new Action1<Void>() {

			@Override
			public void call(Void aVoid) {
				buildAdapter();
			}

		}).first().subscribe();

	}

	private void buildAdapter() {
		Print.log("build adapter");

		int graphHeight = mGraph.getHeight() - mGraph.getPaddingTop() - mGraph.getPaddingBottom() - (mComponentDecorator.getSpacing() * 2);
		int componentWidth = (int) (graphHeight * .2f); //TODO minus margin

		mAdapter = new ClassPlaylistEditorAdapter(this, mComponentDecorator.getSpacing(), componentWidth, graphHeight);
		mGraph.setAdapter(mAdapter);

		buildPlaylistAdapter();
//		fuck();

		//Start this after the adapter is initialized
//		downloadPlaylistTracks(mSpotifyPlaylist.getTracksUrl());
	}

//	private void fuck() {
//		ArrayList<String> componentStrings = new ArrayList<>();
//		componentStrings.add("Apple");
//		componentStrings.add("Carrot");
//		componentStrings.add("Dog");
//		componentStrings.add("EightCha");
//		componentStrings.add("Rats");
//		componentStrings.add("Apple");
//		componentStrings.add("Carrot");
//		componentStrings.add("Dog");
//		componentStrings.add("EightCha");
//		componentStrings.add("Rats");
//		componentStrings.add("Apple");
//		componentStrings.add("Carrot");
//		componentStrings.add("Dog");
//		componentStrings.add("EightCha");
//		componentStrings.add("Rats");
//
//		ArrayList<ClassmateClassComponent> components = new ArrayList<>();
//		for(String s : componentStrings)
//			components.add(buildComponentFromString(s));
//
//		mAdapter.setItems(components);
//	}

	//TODO remove this
	private ClassmateClassComponent buildComponentFromString(String s) {
		ClassmateClassComponent classmateClassComponent = new ClassmateClassComponent();
		classmateClassComponent.setName(s);

		float f = s.length() / 8f;
		int intensity = Math.round(f * 4);

		classmateClassComponent.setIntensity(intensity);

		return classmateClassComponent;
	}

	private void buildPlaylistAdapter() {
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

		Print.log("add components");

		mAdapter.setComponents(componentStrings);

//		ClassPlaylistEditorAdapter editorAdapter = new ClassPlaylistEditorAdapter(this, barWidth);
//		editorAdapter.setComponents(componentStrings);
//
//		mPlaylist.setAdapter(editorAdapter);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_class_playlist_editor;
	}

}
