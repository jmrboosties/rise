package fitness.classmate.model;

import java.util.ArrayList;

public class ClassmateClassComponent {

	private String mName;
	private ArrayList<ComponentNote> mComponentNotes;
	private ComponentTrack mComponentTrack;
	private float mIntensity = .5f;

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public ArrayList<ComponentNote> getComponentNotes() {
		return mComponentNotes;
	}

	public void setComponentNotes(ArrayList<ComponentNote> componentNotes) {
		mComponentNotes = componentNotes;
	}

	public ComponentTrack getComponentTrack() {
		return mComponentTrack;
	}

	public void setComponentTrack(ComponentTrack componentTrack) {
		mComponentTrack = componentTrack;
	}

	public float getIntensity() {
		return mIntensity;
	}

	public void setIntensity(float intensity) {
		mIntensity = intensity;
	}

}
