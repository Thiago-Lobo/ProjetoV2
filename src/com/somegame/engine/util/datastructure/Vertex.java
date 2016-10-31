package com.ioavthiago.engine.util.datastructure;

public class Vertex {

	public static final int SIZE = 13;
	
	private Vector2f pos;
	private Vector2f uv;
	private Vector3f rgb;
	private float alpha;
	
	private float rotation;
	private Vector2f anchorPoint;
	private Vector2f scaling;
	
	public Vertex() {
		pos = new Vector2f();
		uv = new Vector2f();
		rgb = new Vector3f().setXYZ(1f, 1f, 1f);
		alpha = 1f;
		rotation = 0;
		anchorPoint = new Vector2f();
		scaling = new Vector2f();
	}
	
	public Vector3f getRGB() { return rgb; }
	
	public float getAlpha() { return alpha; }
	
	public Vector2f getPos() { return pos; }
	
	public Vector2f getUV() { return uv; }
	
	public Vector2f getScaling() { return scaling; }
	
	public float getRotation() { return rotation; }
	
	public Vector2f getAnchorPoint() { return anchorPoint; }
	
	public Vertex setRGB(float r, float g, float b) {
		rgb.setXYZ(r, g, b);
		return this;
	}
	
	public Vertex setAlpha(float alpha_) {
		alpha = alpha_;
		return this;
	}
	
	public Vertex setPos(float x, float y) {
		pos.setXY(x, y);
		return this;
	}
	
	public Vertex setUV(float u, float v) {
		uv.setXY(u, v);
		return this;
	}
	
	public Vertex setRotationDeg(float angle) {
		rotation = (float) Math.toRadians(angle);
		return this;
	}

	public Vertex setRotationRad(float angle) {
		rotation = angle;
		return this;
	}

	public Vertex setScaling(float sx, float sy) {
		scaling.setXY(sx, sy);
		return this;
	}
	
	public Vertex setAnchorPoint(float ax, float ay) {
		anchorPoint.setXY(ax, ay);
		return this;
	}
	
}
