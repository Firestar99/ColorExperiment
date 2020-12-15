package colors.entity;

import colors.Rendering;
import colors.util.Vector2d;

public class Entity {
	
	public Vector2d pos = new Vector2d();
	
	public Entity() {
		
	}
	
	public void render(Rendering render) {
		
	}
	
	public void update(Rendering render) {
		
	}
	
	public static Entity setEntityAttribs(Entity ent, Vector2d pos) {
		return setEntityAttribs(ent, pos, new Vector2d());
	}
	
	public static Entity setEntityAttribs(Entity ent, Vector2d pos, Vector2d posrel) {
		ent.pos.set(pos);
		if (ent instanceof EntityWithSpeed)
			((EntityWithSpeed) ent).posrel.set(posrel);
		return ent;
	}
	
}
