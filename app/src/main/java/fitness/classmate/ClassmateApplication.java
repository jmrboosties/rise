package fitness.classmate;

import android.app.Application;
import fitness.classmate.preferences.Preferences;

public class ClassmateApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		Preferences.initialize(this);
	}

}
