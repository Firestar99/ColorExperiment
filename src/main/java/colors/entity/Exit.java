package colors.entity;

import colors.Rendering;
import colors.util.Color;

import static org.lwjgl.opengl.GL11.*;

public class Exit extends Entity {
	
	int tick = -1;
	
	public Exit() {
		
	}
	
	@Override
	public void render(Rendering render) {
		float add;
		if (tick == -1) {
			add = 0;
		} else {
			add = tick / 5f;
		}
		float add2 = add * 2;
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_QUADS);
		
		glVertex2d(-15 + add, 40 + add2);
		glVertex2d(15 - add, 40 + add2);
		glVertex2d(15 - add, 0);
		glVertex2d(-15 + add, 0);
		
		glEnd();
		
		Color.getSinColor(render.player.tick).glColor();
		glBegin(GL_QUADS);
		
		glVertex2d(-12 + add, 37 + add2);
		glVertex2d(12 - add, 37 + add2);
		glVertex2d(12 - add, 3);
		glVertex2d(-12 + add, 3);
		
		glEnd();
		glColor3f(1, 1, 1);
	}
	
	@Override
	public void update(Rendering render) {
		if (render.renderPlayer && render.player.pos.copy().offsetNeg(pos).length() < 15) {
			tick = 30;
			render.renderPlayer = false;
		}
		if (tick > 0) {
			tick--;
		}
		if (tick == 0) {
			render.newLevel = render.currLevel.substring(0, render.currLevel.length() - 1) + (Integer.parseInt(render.currLevel.substring(render.currLevel.length() - 1)) + 1);
		}
	}
	
}
