package fitness.classmate.interfaces;

public interface ClassNote {

	enum Type {
		MOVE, FADEOUT
	}

	long getTimestamp();

	Type getType();

	String getDescription();

}
