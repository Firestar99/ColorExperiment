package colors.entity;

import colors.Init;
import colors.Rendering;
import colors.util.Vector2d;

import static org.lwjgl.opengl.GL11.*;

public class Throw extends Entity {
	
	public Vector2d dir;
	public int tick;
	public int leak;
	
	public Throw(int leak, Vector2d dir) {
		this.leak = leak;
		this.dir = dir;
	}
	
	@Override
	public void render(Rendering render) {
		glColor3f(1, 1, 1);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, Init.throwId);
		glRotated((float) tick / leak * 90f, 0, 0, 1);
		glBegin(GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex2d(-10, -10);
		glTexCoord2d(0, 1);
		glVertex2d(10, -10);
		glTexCoord2d(1, 1);
		glVertex2d(10, 10);
		glTexCoord2d(1, 0);
		glVertex2d(-10, 10);
		glEnd();
	}
	
	@Override
	public void update(Rendering render) {
		tick++;
		if (tick >= leak) {
			Thrown s = new Thrown();
			s.pos = pos.copy();
			s.posrel = dir.copy();
			render.ents.add(s);
			tick = 0;
		}
	}
	
}
