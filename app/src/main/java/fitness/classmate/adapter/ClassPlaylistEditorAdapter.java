package fitness.classmate.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fitness.classmate.base.BaseActivity;
import fitness.classmate.model.ClassmateClassComponent;

public class ClassPlaylistEditorAdapter extends RecyclerView.Adapter {

	private BaseActivity mActivity;

	private ArrayList<ClassmateClassComponent> mComponents = new ArrayList<>();

	public ClassPlaylistEditorAdapter(BaseActivity activity) {
		mActivity = activity;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

	}

	@Override
	public int getItemCount() {
		return mComponents.size();
	}

	public void setComponents(@NonNull ArrayList<ClassmateClassComponent> components) {
		mComponents = components;
		notifyDataSetChanged();
	}

	private class BarViewHolder extends RecyclerView.ViewHolder {

		public BarViewHolder(View itemView) {
			super(itemView);
		}

		public void buildItem() {

		}

	}

}
