package colors.entity;

import colors.Rendering;
import colors.util.Vector2d;

public class Leak extends Entity {
	
	public int tick;
	public int leak;
	
	public Leak(int leak) {
		this.leak = leak;
	}
	
	@Override
	public void render(Rendering render) {
		
	}
	
	@Override
	public void update(Rendering render) {
		tick++;
		if (tick >= leak) {
			SplashDeadly s = new SplashDeadly();
			Vector2d v = Vector2d.random().multiply(5);
			v.y = -15;
			s.pos = pos.copy().offset(v);
			render.ents.add(s);
			tick = 0;
		}
	}
	
}
