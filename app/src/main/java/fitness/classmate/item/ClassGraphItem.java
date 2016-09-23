package fitness.classmate.item;

public class ClassGraphItem {

	private String mTitle;
	private int mType;

	public static final int COMPONENT = 0;
	public static final int PLACEHOLDER = 1;

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public int getType() {
		return mType;
	}

	public void setType(int type) {
		mType = type;
	}

}
