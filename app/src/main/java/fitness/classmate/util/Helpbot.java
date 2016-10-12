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

	/**
	 * If both objects are null, considers them to be equal
	 *
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	@SuppressWarnings("SimplifiableIfStatement")
	public static boolean equalsWithNull(Object obj1, Object obj2) {
		if(obj1 == null && obj2 == null)
			return true;
		else if(obj1 != null && obj2 != null)
			return obj1.equals(obj2);
		else
			return false;
	}

}
