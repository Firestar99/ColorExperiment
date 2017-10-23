package colors;

import colors.util.Vector2d;

import static org.lwjgl.input.Keyboard.*;

public enum Movement {
	
	LEFT(KEY_LEFT, new Vector2d(-1, 0)),
	A(KEY_A, new Vector2d(-1, 0)),
	RIGHT(KEY_RIGHT, new Vector2d(1, 0)),
	D(KEY_D, new Vector2d(1, 0));
	
	public final int key;
	public final Vector2d offset;
	
	Movement(int key, Vector2d offset) {
		this.key = key;
		this.offset = offset;
	}
	
}
