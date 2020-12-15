package colors.entity;

import colors.Movement;
import colors.Rendering;
import colors.util.Color;
import colors.util.Texture;
import colors.util.Vector2d;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.*;

public class Player extends Entity {
	
	public static final int speed = 5;
	public static final int upwards = 9;
	public static final int upTries = 10;
	public static final Vector2d DOWNTINY = new Vector2d(0, -0.5);
	
	Vector2d accX = new Vector2d();
	float up;
	int upstage;
	boolean pressed = false;
	double tick;
	boolean doKill = false;
	public Vector2d killVec = new Vector2d(5, 5);
	
	public Player() {
		pos.set(5, 5);
	}
	
	@Override
	public void update(Rendering render) {
		movement(render.tex, render);
		tick += 0.25;
	}
	
	public void movement(Texture tex, Rendering render) {
		if (!render.renderPlayer)
			return;
		up = up - EntityWithSpeed.gvt;
		
		if (isKeyDown(KEY_SPACE) || isKeyDown(KEY_UP) || isKeyDown(KEY_W)) {
			if (!pressed) {
				pressed = true;
				
				boolean ret = false;
				label:
				for (int x = -15; x <= 15; x++) {
					for (int y = -13; y < -9; y++) {
						Color color = tex.get(x + (int) pos.x, y + (int) pos.y);
						if (color != null) {
							if (color.getR() != 0) {
								ret = true;
								break label;
							}
						}
					}
				}
				
				if (ret) {
					makeSplash(render, 3, 2);
					up = upwards;
					upstage = 0;
				} else if (upstage == 0) {
					makeSplash(render, 8, 3);
					upstage++;
					up = upwards;
				}
				
			}
		} else {
			pressed = false;
		}
		
		Vector2d vec = new Vector2d();
		for (Movement m : Movement.values()) {
			if (m == null)
				continue;
			if (isKeyDown(m.key))
				vec.offset(m.offset);
		}
		
		vec.normalizeSquared();
		
		Vector2d oldPos = pos.copy();
		vec.multiply(speed);
		for (int k = 0; k < upTries; k++) {
			pos.offset(vec);
			if (!isPossibleToGoTo(tex, render, false)) {
				pos.set(oldPos);
				vec.offset(0, 1);
			} else {
				accX.x += vec.x / speed;
				break;
			}
		}
		oldPos.set(pos);
		
		pos.offset(0, up);
		if (!isPossibleToGoTo(tex, render, true)) {
			pos.set(oldPos);
		}
		accX.offset(DOWNTINY).normalize().multiply(5);
		
		if (doKill)
			kill(render);
	}
	
	public void makeSplash(Rendering render, int min, int randMax) {
		for (int i = 0; i < Vector2d.rand.nextInt(randMax) + min; i++) {
			Splash sp = new Splash();
			sp.pos.set(pos);
			sp.posrel.set(Vector2d.random().offset(0, 2).normalizeSafe(Vector2d.DOWN).multiply(Vector2d.rand.nextInt(5) + 7));
			render.ents.add(sp);
		}
	}
	
	public boolean isPossibleToGoTo(Texture tex, Rendering render, boolean allowTouchGround) {
		boolean ret = true;
		if (pos.x < 10)
			ret = false;
		if (pos.x > tex.width - 11)
			ret = false;
		if (pos.y < 10) {
			ret = false;
			onTouchGround(render);
		}
		if (pos.y > tex.height - 11)
			ret = false;
		
		for (int x = -10; x <= 10; x++) {
			for (int y = -7; y <= 10; y++) {
				Color color = tex.get(x + (int) pos.x, y + (int) pos.y);
				if (color == null) {
					ret = false;
				} else {
					if (color.getR() != 0) {
						ret = false;
					}
					doChecks(color, render);
				}
			}
		}
		
		for (int x = -10; x <= 10; x++) {
			for (int y = -10; y < -7; y++) {
				Color color = tex.get(x + (int) pos.x, y + (int) pos.y);
				if (color == null) {
					ret = false;
				} else {
					if (color.getR() != 0) {
						if (allowTouchGround)
							onTouchGround(render);
						ret = false;
					}
					doChecks(color, render);
				}
			}
		}
		return ret;
	}
	
	public void kill(Rendering render) {
		makeSplash(render, 50, 1);
		killWithoutParticles(render);
	}
	
	public void killWithoutParticles(Rendering render) {
		pos.set(killVec);
		accX.reset();
		onTouchGround(render);
		doKill = false;
	}
	
	public void doChecks(Color c, Rendering render) {
		if (c.getB() == 100) {
			doKill = true;
		}
	}
	
	public void onTouchGround(Rendering render) {
		upstage = 0;
		up = 0;
		Texture.colorAreaCircle(render.tex, render.colored, pos, Color.getSinColor(tick), 18);
	}
	
	@Override
	public void render(Rendering render) {
		glPushMatrix();
		pos.glTranslate();
		
		float u = up;
		if (u < 0)
			u = u * 0.5f;
		
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_QUADS);
		glVertex2d(-10, -10);
		glVertex2d(-10 - accX.x, 10 + u * 2);
		glVertex2d(10 - accX.x, 10 + u * 2);
		glVertex2d(10, -10);
		glEnd();
		
		glPopMatrix();
	}
	
}
