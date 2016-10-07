package fitness.classmate.activity;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import fitness.classmate.R;
import fitness.classmate.adapter.ClassAdapter;
import fitness.classmate.base.BaseActivity;
import fitness.classmate.model.ClassmateClass;

import java.util.ArrayList;

public class ClassListActivity extends BaseActivity {

	private ClassAdapter mClassAdapter;

	@Override
	protected int getMenuResId() {
		return R.menu.menu_create_new;
	}

	@Override
	protected void prepareActivity() {

	}

	@Override
	protected void initLayout() {
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

		LinearLayoutManager manager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(manager);

		mClassAdapter = new ClassAdapter(this);
		recyclerView.setAdapter(mClassAdapter);

		loadList();
	}

	private void loadList() {
		new AsyncTask<Void, Void, ArrayList<ClassmateClass>>() {

			@Override
			protected ArrayList<ClassmateClass> doInBackground(Void... params) {
				ArrayList<ClassmateClass> fakeClasses = new ArrayList<>();

				fakeClasses.add(buildClass());
				fakeClasses.add(buildClass());
				fakeClasses.add(buildClass());
				fakeClasses.add(buildClass());
				fakeClasses.add(buildClass());
				fakeClasses.add(buildClass());

				return fakeClasses;
			}

			@Override
			protected void onPostExecute(ArrayList<ClassmateClass> classmateClasses) {
				mClassAdapter.setClasses(classmateClasses);
			}

		}.execute();
	}

	private ClassmateClass buildClass() {
		ClassmateClass classmateClass = new ClassmateClass();
		classmateClass.setAuthor("brett");
		classmateClass.setTitle("fun spin class");
		classmateClass.setId(1);
		classmateClass.setCreatedAt("10/12/16");

		return classmateClass;
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.basic_swipe_refresh_recyclerview;
	}

}
