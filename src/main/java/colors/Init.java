package colors;

import colors.util.Texture;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Init {
	
	public static int splashId = -1;
	public static Texture leakTex;
	public static int throwId = -1;
	public static int thrownId = -1;
	
	public static void init() throws Exception {
		splashId = makeTexture("Splash.png");
		throwId = makeTexture("Throw.png");
		thrownId = makeTexture("Thrown.png");
		leakTex = Texture.setupTexture("Leak.png", false);
	}
	
	public static void release() {
		if (splashId != -1)
			glDeleteTextures(splashId);
	}
	
	public static int makeTexture(String tex) throws IOException {
		Texture splashTex = Texture.setupTexture(tex, false);
		int Id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, Id);
		splashTex.setupParameter();
		splashTex.upload();
		return Id;
	}
	
}
