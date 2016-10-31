package com.somegame.engine.rendering;

public class TextureArea {

	private float u1;
	private float v1;
	private float u2;
	private float v2;

	public TextureArea(float _u1, float _v1, float _u2, float _v2) {
		u1 = _u1;
		v1 = _v1;
		u2 = _u2;
		v2 = _v2;
	}

	public float getU1() {
		return u1;
	}

	public float getV1() {
		return v1;
	}

	public float getU2() {
		return u2;
	}

	public float getV2() {
		return v2;
	}
	
	public String toString() {
		return "u1: " + u1 + " y1: " + v1 + " x2: " + u2 + " y2: " + v2; 
	}
	
}
