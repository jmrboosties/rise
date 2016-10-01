package fitness.classmate.model;

import android.support.annotation.NonNull;
import fitness.classmate.interfaces.ClassNote;

import java.util.ArrayList;

public class SpotifyPlaylistTrack {

	private String mName;
	private String mArtist;
	private long mDuration;
	private String mUri;
	private String mImageUrl;
	private SpotifyAudioFeatures mAudioFeatures;

	private ArrayList<ClassNote> mClassNotes = new ArrayList<>();

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getArtist() {
		return mArtist;
	}

	public void setArtist(String artist) {
		mArtist = artist;
	}

	public long getDuration() {
		return mDuration;
	}

	public void setDuration(long duration) {
		mDuration = duration;
	}

	public String getUri() {
		return mUri;
	}

	public void setUri(String uri) {
		mUri = uri;
	}

	public ArrayList<ClassNote> getClassNotes() {
		return mClassNotes;
	}

	public void addClassNote(ClassNote classNote) {
		if(mClassNotes == null)
			mClassNotes = new ArrayList<>();

		mClassNotes.add(classNote);
	}

	public void setClassNotes(@NonNull ArrayList<ClassNote> classNotes) {
		mClassNotes = classNotes;
	}

	public String getImageUrl() {
		return mImageUrl;
	}

	public void setImageUrl(String imageUrl) {
		mImageUrl = imageUrl;
	}

	public SpotifyAudioFeatures getAudioFeatures() {
		return mAudioFeatures;
	}

	public void setAudioFeatures(SpotifyAudioFeatures audioFeatures) {
		mAudioFeatures = audioFeatures;
	}
}
