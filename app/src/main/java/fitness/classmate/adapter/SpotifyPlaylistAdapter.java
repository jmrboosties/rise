package fitness.classmate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fitness.classmate.R;
import fitness.classmate.model.SpotifyPlaylist;

import java.util.ArrayList;

public class SpotifyPlaylistAdapter extends RecyclerView.Adapter {

	private Context mContext;
	private ArrayList<SpotifyPlaylist> mSpotifyPlaylists = new ArrayList<>();

	private OnPlaylistClickedListener mOnPlaylistClickedListener;

	public SpotifyPlaylistAdapter(Context context) {
		mContext = context;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new SpotifyPlaylistViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_spotify_playlist, parent, false));
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if(holder instanceof SpotifyPlaylistViewHolder)
			((SpotifyPlaylistViewHolder) holder).buildItem();
	}

	@Override
	public int getItemCount() {
		return mSpotifyPlaylists.size();
	}

	public void setSpotifyPlaylists(ArrayList<SpotifyPlaylist> spotifyPlaylists) {
		mSpotifyPlaylists = spotifyPlaylists;
		notifyDataSetChanged();
	}

	public void setOnPlaylistClickedListener(OnPlaylistClickedListener onPlaylistClickedListener) {
		mOnPlaylistClickedListener = onPlaylistClickedListener;
	}

	public class SpotifyPlaylistViewHolder extends RecyclerView.ViewHolder {

		private TextView mTitle;
		private TextView mSubtitle;

		public SpotifyPlaylistViewHolder(View itemView) {
			super(itemView);

			mTitle = (TextView) itemView.findViewById(R.id.lsp_title);
			mSubtitle = (TextView) itemView.findViewById(R.id.lsp_subtitle);

			itemView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mOnPlaylistClickedListener != null)
						mOnPlaylistClickedListener.onPlaylistClicked(mSpotifyPlaylists.get(getAdapterPosition()));
				}

			});

			itemView.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if(mOnPlaylistClickedListener != null) {
						mOnPlaylistClickedListener.onPlaylistLongClicked(mSpotifyPlaylists.get(getAdapterPosition()));
						return true;
					}
					else
						return false;
				}

			});
		}

		public void buildItem() {
			mTitle.setText(mSpotifyPlaylists.get(getAdapterPosition()).getName());
			mSubtitle.setText(mContext.getString(R.string.track_count, mSpotifyPlaylists.get(getAdapterPosition()).getTrackCount()));
		}

	}

	public interface OnPlaylistClickedListener {

		void onPlaylistClicked(SpotifyPlaylist playlist);

		void onPlaylistLongClicked(SpotifyPlaylist playlist);

	}

}
