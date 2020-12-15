package colors.util;

import org.lwjgl.opengl.GL11;

import java.util.Random;

import static java.lang.Math.*;

public class Color {
	
	public static final Random rand = new Random();
	
	public byte r;
	public byte g;
	public byte b;
	
	public Color(byte r, byte g, byte b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Color(float r, float g, float b) {
		this(convertToByte(r), convertToByte(g), convertToByte(b));
	}
	
	public Color(int r, int g, int b) {
		this((byte) (r & 0xFF), (byte) (g & 0xFF), (byte) (b & 0xFF));
		if (r == 256) {
			this.r = (byte) 255;
		}
		if (g == 256) {
			this.g = (byte) 255;
		}
		if (b == 256) {
			this.b = (byte) 255;
		}
	}
	
	public static byte convertToByte(float f) {
		if (f == 1)
			return (byte) -1;
		return (byte) ((int) (f * 256) & 0xFF);
	}
	
	public boolean isBlack() {
		return r == 0 && g == 0 && b == 0;
	}
	
	public int add() {
		return r + g + b;
	}
	
	public void glColor() {
		GL11.glColor3ub(r, g, b);
	}
	
	public static Color random() {
		return new Color((byte) (rand.nextInt(256) - 128), (byte) (rand.nextInt(256) - 128), (byte) (rand.nextInt(256) - 128));
	}
	
	public static Color randomSin() {
		return getSinColor(rand.nextInt(360));
	}
	
	public static Color getSinColor(double tick) {
		return new Color(((int) ((sin(toRadians(tick)) + 1) * 128)), ((int) ((sin(toRadians(tick + 120)) + 1) * 128)), ((int) ((sin(toRadians(tick + 240)) + 1) * 128)));
	}
	
	public int getR() {
		return Byte.toUnsignedInt(r);
	}
	
	public int getG() {
		return Byte.toUnsignedInt(g);
	}
	
	public int getB() {
		return Byte.toUnsignedInt(b);
	}
	
	@Override
	public String toString() {
		return "r:" + getR() + ", g:" + getG() + ", b:" + getB();
	}
	
}
