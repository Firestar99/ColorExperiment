package colors.util;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;
import java.util.Random;

import static java.lang.Math.sqrt;

public class Vector2d {
	
	public static final Random rand = new Random();
	public static final Vector2d ZERO = new Vector2d();
	public static final Vector2d DOWN = new Vector2d(0, -1);
	
	public double x;
	public double y;
	
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2d() {
		this(0, 0);
	}
	
	public Vector2d set(Vector2d vec) {
		this.x = vec.x;
		this.y = vec.y;
		return this;
	}
	
	public Vector2d set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2d offset(Vector2d vec) {
		this.x += vec.x;
		this.y += vec.y;
		return this;
	}
	
	public Vector2d offset(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vector2d offsetNeg(Vector2d vec) {
		this.x -= vec.x;
		this.y -= vec.y;
		return this;
	}
	
	public Vector2d multiply(double amount) {
		this.x *= amount;
		this.y *= amount;
		return this;
	}
	
	public Vector2d multipy(Vector2d vec) {
		this.x *= vec.x;
		this.y *= vec.y;
		return this;
	}
	
	public Vector2d divide(Vector2d vec) {
		this.x /= vec.x;
		this.y /= vec.y;
		return this;
	}
	
	public Vector2d copy() {
		return new Vector2d(x, y);
	}
	
	public Vector2d normalizeSafe(Vector2d vec) {
		double l = length();
		if (l == 0) {
			return vec;
		}
		x = x / l;
		y = y / l;
		return this;
	}
	
	public Vector2d normalize() {
		double l = length();
		if (l != 0) {
			x = x / l;
			y = y / l;
		}
		return this;
	}
	
	public Vector2d normalizeSquared() {
		double l = lengthSquared();
		if (l != 0) {
			x = x / l;
			y = y / l;
		}
		return this;
	}
	
	public double length() {
		return sqrt(x * x + y * y);
	}
	
	public double lengthSquared() {
		return x * x + y * y;
	}
	
	public void glTranslate() {
		GL11.glTranslated(x, y, 0);
	}
	
	public void glVertex() {
		GL11.glVertex2d(x, y);
	}
	
	public void glVertex3d() {
		GL11.glVertex3d(x, y, 0);
	}
	
	public void glMultMatrix() {
		DoubleBuffer buffer = BufferUtils.createDoubleBuffer(16);
		buffer.put(new double[] {
				x, y, 0, 0,
				-y, x, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1,
		});
		buffer.flip();
		GL11.glMultMatrix(buffer);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{x: ");
		builder.append(x);
		builder.append(", y: ");
		builder.append(y);
		builder.append("}");
		return builder.toString();
	}
	
	public static Vector2d random() {
		return new Vector2d(rand.nextInt(2000) / 1000d - 1, rand.nextInt(2000) / 1000d - 1);
	}
	
	public Vector2d negate() {
		x = -x;
		y = -y;
		return this;
	}
	
	public void reset() {
		set(0, 0);
	}
	
}
