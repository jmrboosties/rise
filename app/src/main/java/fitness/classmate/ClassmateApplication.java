package fitness.classmate;

import android.app.Application;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import fitness.classmate.preferences.Preferences;

public class ClassmateApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		Preferences.initialize(this);

		FlowManager.init(new FlowConfig.Builder(this).openDatabasesOnInit(true).build());
	}

}
