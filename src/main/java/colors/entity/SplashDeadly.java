package colors.entity;

import colors.Init;
import colors.Rendering;
import colors.util.Color;
import colors.util.Texture;
import colors.util.Vector2d;

import static org.lwjgl.opengl.GL11.*;

public class SplashDeadly extends EntityWithSpeed {
	
	public static final Color c = new Color(1f, 1f, 1f);
	
	public SplashDeadly() {
		
	}
	
	@Override
	public void update(Rendering render) {
		pos.offset(posrel);
		if (render.player.pos.copy().offsetNeg(pos).length() < 7)
			render.player.kill(render);
		if (!Splash.isPossibleToGoTo(render.tex, pos)) {
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
	
}
