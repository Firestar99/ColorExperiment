package colors.entity;

import colors.Rendering;
import colors.util.Vector2d;

public class EntityWithSpeed extends Entity {
	
	public static final float gvt = 0.5f;
	public Vector2d posrel = new Vector2d();
	
	public EntityWithSpeed() {
		
	}
	
	public void gravity() {
		posrel.offset(0, -gvt);
	}
	
	@Override
	public void update(Rendering render) {
		pos.offset(posrel);
		gravity();
	}
	
}
