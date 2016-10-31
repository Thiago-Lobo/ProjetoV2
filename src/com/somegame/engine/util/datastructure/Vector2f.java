package com.ioavthiago.engine.util.datastructure;

/*
 * Abstração de um Vetor 2D.
 * Extende DataPacket para poder ser agrupado em DataPools.
 * Implementa getters e setters e as operações vetoriais básicas.
 * 
 */

public class Vector2f {

	private float x;
	private float y;
	
	public Vector2f() {
		x = 0f;
		y = 0f;
	}
	
	public Vector2f(float x, float y) {
		setX(x);
		setY(y);
	}

	public float abs() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public float dot(Vector2f vec) {
		return x * vec.getX() + y * vec.getY();
	}
	
	public Vector2f normalize() {
		float abs = abs();
		
		x /= abs;
		y /= abs;
		
		return this;
	}
	
	public boolean equals(Vector2f vec) {
		return (x == vec.getX() && y == vec.getY());
	}
	
	public Vector2f addX(float f) {
		setX(getX() + f);
		return this;
	}
	
	public Vector2f addY(float f) {
		setY(getY() + f);
		return this;
	}
	
	public Vector2f negate() {
		return this.setXY(-x, -y);
	}
	
	public Vector2f clone() {
		return new Vector2f().setXY(x, y);
	}
	
	public Vector2f scale(float x, float y, float anchorX, float anchorY) {
		float xx = getX() - anchorX;
		float yy = getY() - anchorY;
		
		return this.setXY(x * xx + anchorX, y * yy + anchorY);
	}
	
	public Vector2f scale(float x, float y) {
		return scale(x, y, 0f, 0f);
	}
	
	public Vector2f rotate(float angle, float anchorX, float anchorY) {
		float xx = x - anchorX;
		float yy = y - anchorY;
		
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		
		return this.setXY((float) (xx * cos - yy * sin) + anchorX, (float) (xx * sin + yy * cos) + anchorY);
	}
	
	public Vector2f rotate(float angle) {
		return rotate(angle, 0f, 0f);
	}
	
	public Vector2f add(Vector2f vec) {
		return this.setXY(x + vec.getX(), y + vec.getY());
	}
	
	public Vector2f add(float f) {
		return this.setXY(x + f, y + f);
	}
	
	public Vector2f sub(Vector2f vec) {
		return this.setXY(x - vec.getX(), y - vec.getY());
	}
	
	public Vector2f sub(float f) {
		return this.setXY(x - f, y - f);
	}

	public Vector2f mul(Vector2f vec) {
		return this.setXY(x * vec.getX(), y * vec.getY());
	}
	
	public Vector2f mul(float f) {
		return this.setXY(x * f, y * f);
	}
	
	public Vector2f div(Vector2f vec) {
		return this.setXY(x / vec.getX(), y / vec.getY());
	}
	
	public Vector2f div(float f) {
		return this.setXY(x / f, y / f);
	}
	
	public Vector2f setXY(float x, float y) {
		setX(x);
		setY(y);
		return this;
	}
	
	public float getX() {
		return x;
	}

	public Vector2f setX(float x) {
		this.x = x;
		return this;
	}
	
	public float getY() {
		return y;
	}

	public Vector2f setY(float y) {
		this.y = y;
		return this;
	}
	
	public int getYInt() {
		return (int) y;
	}
	
	public int getXInt() {
		return (int) x;
	}
	
	public String toString() {
		return "X: " + x + " Y: " + y;
	}
	
}
