package fitness.classmate.model;

import java.util.ArrayList;

import fitness.classmate.annotation.ColumnName;
import fitness.classmate.database.Tables;

public class ClassmateClassComponent {

	@ColumnName(Tables.ClassmateClassComponents.NAME)
	private String mName;

	private ArrayList<ComponentNote> mComponentNotes;

	private ComponentTrack mComponentTrack;

	//Only 1-5
	@ColumnName(Tables.ClassmateClassComponents.PACE)
	private int mIntensity = 3;

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

	public int getIntensity() {
		return mIntensity;
	}

	public void setIntensity(int intensity) {
		if(intensity < 1 || intensity > 5)
			throw new IllegalArgumentException("intensity is 1-5");

		mIntensity = intensity;
	}

}
