package colors.entity;

import colors.Init;
import colors.Rendering;
import colors.util.Color;
import colors.util.Texture;
import colors.util.Vector2d;

import static org.lwjgl.opengl.GL11.*;

public class Thrown extends EntityWithSpeed {
	
	public Thrown() {
		
	}
	
	@Override
	public void update(Rendering render) {
		pos.offset(posrel);
		if (render.player.pos.copy().offsetNeg(pos).length() < 15)
			render.player.kill(render);
		if (!isPossibleToGoTo(render.tex)) {
			colorAreaCircle(render.tex, render.colored, pos, Rendering.WHITE, Color.rand.nextInt(8) + 16);
			render.ents.remove(this);
		}
	}
	
	@Override
	public void render(Rendering render) {
		glColor3f(1, 1, 1);
		posrel.copy().normalizeSafe(Vector2d.DOWN).glMultMatrix();
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, Init.thrownId);
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
	
	public boolean isPossibleToGoTo(Texture tex) {
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
	
	public static void colorAreaCircle(Texture tex, Texture color, Vector2d off, Color c, int range) {
		Vector2d pos = new Vector2d();
		for (int x = -range; x <= range; x++) {
			for (int y = -range; y <= range; y++) {
				pos.set(x, y);
				if (pos.length() > range)
					continue;
				pos.offset(off);
				Color cl = tex.get((int) pos.x, (int) pos.y);
				if (cl != null && cl.add() != 0) {
					color.set((int) pos.x, (int) pos.y, c);
				}
			}
		}
	}
	
}
