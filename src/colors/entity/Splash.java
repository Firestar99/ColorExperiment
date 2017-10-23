package colors.entity;

import colors.Init;
import colors.Rendering;
import colors.util.Color;
import colors.util.Texture;
import colors.util.Vector2d;

import static org.lwjgl.opengl.GL11.*;

public class Splash extends EntityWithSpeed {
	
	Color c;
	
	public Splash(Color c) {
		this.c = c;
	}
	
	public Splash() {
		this(Color.random());
	}
	
	@Override
	public void update(Rendering render) {
		pos.offset(posrel);
		if (!isPossibleToGoTo(render.tex, pos)) {
			Texture.colorAreaCircle(render.tex, render.colored, pos, c, Color.rand.nextInt(6) + 10);
			render.ents.remove(this);
		}
		gravity();
	}
	
	@Override
	public void render(Rendering render) {
		posrel.copy().normalizeSafe(Vector2d.DOWN).glMultMatrix();
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, Init.splashId);
		c.glColor();
		glBegin(GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex2d(-5, -5);
		glTexCoord2d(0, 1);
		glVertex2d(5, -5);
		glTexCoord2d(1, 1);
		glVertex2d(5, 5);
		glTexCoord2d(1, 0);
		glVertex2d(-5, 5);
		glEnd();
		glColor3f(1, 1, 1);
	}
	
	public static boolean isPossibleToGoTo(Texture tex, Vector2d pos) {
		boolean ret = true;
		if (pos.x < 10)
			ret = false;
		if (pos.x > tex.width - 11)
			ret = false;
		if (pos.y < 10)
			ret = false;
		if (pos.y > tex.height - 11)
			ret = false;
		
		for (int x = -10; x <= 10; x++) {
			for (int y = -10; y <= 10; y++) {
				Color color = tex.get(x + (int) pos.x, y + (int) pos.y);
				if (color == null) {
					ret = false;
				} else {
					if (color.getR() != 0) {
						ret = false;
					}
				}
			}
		}
		return ret;
	}
	
}
