package colors.util;

import colors.Rendering;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {
	
	public final int width;
	public final int height;
	public final ByteBuffer buffer;
	
	public Texture(int width, int height, ByteBuffer buffer) {
		this.width = width;
		this.height = height;
		this.buffer = buffer;
	}
	
	public boolean set(int x, int y, Color c) {
		int pos = getPos(x, y);
		if (pos == -1)
			return false;
		buffer.put(pos + 0, c.r);
		buffer.put(pos + 1, c.g);
		buffer.put(pos + 2, c.b);
		return true;
	}
	
	public Color get(int x, int y) {
		int pos = getPos(x, y);
		if (pos == -1)
			return null;
		return new Color(buffer.get(pos), buffer.get(pos + 1), buffer.get(pos + 2));
	}
	
	public int getPos(int x, int y) {
		if (x <= 0 || y <= 0 || x > width || y > height)
			return -1;
		int pos = (x + ((height - 1 - y) * width)) * 4;
		if (pos < 0 || pos >= buffer.capacity())
			return -1;
		return pos;
	}
	
	public void upload() {
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
	}
	
	public void setupParameter() {
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glGenerateMipmap(GL_TEXTURE_2D);
	}
	
	public static Texture setupTexture(String path, boolean isLevel) throws IOException {
		InputStream in = null;
		try {
			if (isLevel) {
				in = ResourceLoader.getLevel(path);
			} else {
				in = ResourceLoader.getResource(path);
			}
			
			PNGDecoder dec = new PNGDecoder(in);
			int height = dec.getHeight();
			int width = dec.getWidth();
			ByteBuffer buffer = BufferUtils.createByteBuffer(height * width * 4);
			dec.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			return new Texture(width, height, buffer);
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				
			}
		}
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
				if (cl != null && cl.getR() != 0) {
					if (cl.getB() == 100) {
						color.set((int) pos.x, (int) pos.y, Rendering.WHITE);
					} else {
						color.set((int) pos.x, (int) pos.y, c);
					}
				}
			}
		}
	}
	
}
