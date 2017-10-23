package colors;

import colors.entity.Entity;
import colors.entity.Exit;
import colors.entity.Leak;
import colors.entity.Player;
import colors.entity.Throw;
import colors.util.Color;
import colors.util.Texture;
import colors.util.Vector2d;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.lwjgl.input.Keyboard.KEY_ESCAPE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Rendering {
	
	public static final Color WHITE = new Color((byte) -1, (byte) -1, (byte) -1);
	public static final Color BLACK = new Color((byte) 0, (byte) 0, (byte) 0);
	public static final String EMPTY = "";
	
	public final PlayMode mode;
	public boolean isRunning = true;
	public int displayListQuad = -1;
	public Player player = new Player();
	public boolean renderPlayer = true;
	public ArrayList<Entity> ents = new ArrayList<>();
	public String newLevel = null;
	public String currLevel = null;
	
	public int texId = -1;
	public Texture tex;
	public int coloredId = -1;
	public Texture colored;
	
	public Rendering(PlayMode mode) {
		this.mode = mode;
	}
	
	public void loop(String path) throws Exception {
		newLevel = path;
		try {
			DisplayMode dispmode = Display.getDesktopDisplayMode();
			Display.setDisplayModeAndFullscreen(dispmode);
			Display.create();
			Keyboard.create();
			Mouse.create();
			Mouse.setGrabbed(true);
			
			double upDown = ((double) dispmode.getWidth()) / dispmode.getHeight();
			glMatrixMode(GL_PROJECTION_MATRIX);
			glLoadIdentity();
			if (upDown > (800d / 600d)) {
				glOrtho(0, 600 * upDown, 0, 600, -1, 1);
			} else {
				glOrtho(0, 800, 0, 800 / upDown, -1, 1);
			}
			glMatrixMode(GL_MODELVIEW_MATRIX);
			
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glDepthMask(false);
			glEnable(GL_TEXTURE_2D);
			
			displayListQuad = glGenLists(1);
			Init.init();
			checkError();
			
			while (isRunning) {
				if (newLevel != null && !EMPTY.equals(newLevel)) {
					loadLevel();
				}
				//render
				glPushMatrix();
				translateToPlayer();
				glEnable(GL_TEXTURE_2D);
				glBindTexture(GL_TEXTURE_2D, texId);
				colored.upload();
				glGenerateMipmap(GL_TEXTURE_2D);
				glCallList(displayListQuad);
				for (Entity ent : ents) {
					glPushMatrix();
					ent.pos.glTranslate();
					ent.render(this);
					glPopMatrix();
				}
				if (renderPlayer)
					player.render(this);
				glPopMatrix();
				
				//update
				player.update(this);
				for (Entity ent : new ArrayList<>(ents)) {
					ent.update(this);
				}
				
				//display update
				Display.update();
				checkError();
				Display.sync(60);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				if (Keyboard.isKeyDown(KEY_ESCAPE) || Display.isCloseRequested())
					isRunning = false;
			}
			
		} finally {
			try {
				if (displayListQuad != -1)
					glDeleteLists(displayListQuad, 1);
				if (texId != -1)
					glDeleteTextures(texId);
				if (coloredId != -1)
					glDeleteTextures(coloredId);
				
				Init.release();
				
				Mouse.destroy();
				Keyboard.destroy();
				Display.destroy();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	public void checkError() {
		int glError = glGetError();
		if (glError != GL_NO_ERROR) {
			System.err.println("GLError: " + Util.translateGLErrorString(glError));
		}
	}
	
	public void translateToPlayer() {
		Vector2d pos = player.pos.copy().offset(-400, -300);
		if (pos.x < 0)
			pos.x = 0;
		if (pos.x > tex.width - 800)
			pos.x = tex.width - 800;
		if (pos.y < 0)
			pos.y = 0;
		if (pos.y > tex.height - 600)
			pos.y = tex.height - 600;
		pos.negate().glTranslate();
	}
	
	public void drawOnTexture(Texture t, Vector2d pos) {
		drawOnTexture(t, pos, 1);
	}
	
	public void drawOnTexture(Texture t, Vector2d pos, int size) {
		drawOnTexture(t, pos, size, WHITE);
	}
	
	public void drawOnTexture(Texture t, Vector2d pos, double d, Color c) {
		Vector2d drawPos = new Vector2d();
		for (int x = 0; x < t.width; x++) {
			for (int y = 0; y < t.height; y++) {
				Color f = t.get(x, y);
				drawPos.set(-t.width / 2 + x, -t.height / 2 + y).multiply(d).offset(pos);
				if (f != null) {
					if (f.getR() != 0) {
						tex.set((int) drawPos.x, (int) drawPos.y, WHITE);
					}
					if (f.getB() != 0) {
						colored.set((int) drawPos.x, (int) drawPos.y, c);
					}
				}
			}
		}
	}
	
	public void loadLevel() throws IOException {
		currLevel = newLevel;
		newLevel = null;
		ents.clear();
		
		tex = Texture.setupTexture(currLevel + ".png", true);
		texId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texId);
		tex.setupParameter();
		tex.upload();
		
		switch (mode) {
			case NORMAL: {
				ByteBuffer cbuffer = BufferUtils.createByteBuffer(tex.buffer.capacity());
				cbuffer.put(tex.buffer);
				cbuffer.flip();
				colored = new Texture(tex.width, tex.height, cbuffer);
			}
			break;
			case HIDDEN: {
				byte[] black = new byte[] {0, 0, 0, -1};
				ByteBuffer cbuffer = BufferUtils.createByteBuffer(tex.buffer.capacity());
				while (cbuffer.hasRemaining()) {
					cbuffer.put(black);
				}
				cbuffer.flip();
				colored = new Texture(tex.width, tex.height, cbuffer);
			}
			break;
		}
		
		for (int x = 0; x < tex.width; x++) {
			for (int y = 0; y < tex.height; y++) {
				Color f = tex.get(x, y);
				if (f != null) {
					int g = f.getG();
					
					//exit
					if (g == 98) {
						Exit exit = new Exit();
						exit.pos.set(x, y);
						ents.add(exit);
					}
					
					//spawn
					if (g == 99) {
						player.pos.set(x, y);
						player.killVec.set(x, y);
					}
					
					//leak
					if (g >= 100 && g < 120) {
						Leak leak = new Leak((g - 100) * 5);
						leak.pos.set(x, y);
						ents.add(leak);
						drawOnTexture(Init.leakTex, leak.pos, 0.5d, WHITE);
					}
					
					//throw
					if (g >= 120 && g < 140) {
						Color c = tex.get(x, y + 1);
						if (c != null) {
							tex.set(x, y + 1, BLACK);
							Throw leak = new Throw((g - 120) * 5, new Vector2d(c.getR() - 127, c.getB() - 127));
							leak.pos.set(x, y);
							ents.add(leak);
						}
					}
				}
			}
		}
		
		renderPlayer = true;
		player.killWithoutParticles(this);
		
		glNewList(displayListQuad, GL_COMPILE);
		glBegin(GL_QUADS);
		glTexCoord2d(0, 1);
		glVertex2d(0, 0);
		glTexCoord2d(1, 1);
		glVertex2d(tex.width, 0);
		glTexCoord2d(1, 0);
		glVertex2d(tex.width, tex.height);
		glTexCoord2d(0, 0);
		glVertex2d(0, tex.height);
		glEnd();
		glEndList();
	}
	
	public enum PlayMode {
		
		NORMAL,
		HIDDEN
		
	}
	
}
