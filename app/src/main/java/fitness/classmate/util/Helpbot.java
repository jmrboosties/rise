package fitness.classmate.util;

import java.util.concurrent.TimeUnit;

public class Helpbot {

	public static String getDurationTimestampFromMillis(long millis) {
		return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
	}

	public static long getMillisFromTimestamp(String timestamp) {
		String[] split = timestamp.split(":");

		int minutes;
		int seconds;
		if(split[0].length() == 0)
			minutes = 0;
		else
			minutes = Integer.parseInt(split[0]);

		if(split[1].length() == 0)
			seconds = 0;
		else
			seconds = Integer.parseInt(split[1]);

		return TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(seconds);
	}

}
