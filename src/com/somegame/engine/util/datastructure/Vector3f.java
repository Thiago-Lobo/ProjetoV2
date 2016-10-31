package com.ioavthiago.engine.util.datastructure;

public class Vector3f {
	
	private float x;
	private float y;
	private float z;
	
	public Vector3f() {
		x = 0f;
		y = 0f;
		z = 0f;
	}
	
	public Vector3f(float xx, float yy, float zz) {
		setX(xx);
		setY(yy);
		setZ(zz);
	}
	
	public float abs() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public float dot(Vector3f vec) {
		return x * vec.getX() + y * vec.getY() + z * vec.getZ();
	}

	public Vector3f cross(Vector3f vec) {
		float xx = y * vec.getZ() - z * vec.getY();
		float yy = z * vec.getX() - x * vec.getZ();
		float zz = x * vec.getY() - y * vec.getX();
		
		return new Vector3f().setXYZ(xx, yy, zz);
	}
	
	public Vector3f normalize() {
		float abs = abs();
		
		x /= abs;
		y /= abs;
		z /= abs;
		
		return this;
	}
	
	public boolean equals(Vector3f vec) {
		return (x == vec.getX() && y == vec.getY() && z == vec.getZ());
	}
	
	public Vector3f clone() {
		return new Vector3f().setXYZ(x, y, z);
	}
	
	public Vector3f negate() {
		return this.setXYZ(-x, -y, -z);
	}
	
	public Vector3f setXYZ(float x, float y, float z) {
		setX(x);
		setY(y);
		setZ(z);
		return this;
	}
	
	public Vector3f add(Vector3f vec) {
		return this.setXYZ(x + vec.getX(), y + vec.getY(), z + vec.getZ());
	}
	
	public Vector3f add(float f) {
		return this.setXYZ(x + f, y + f, z + f);
	}
	
	public Vector3f sub(Vector3f vec) {
		return this.setXYZ(x - vec.getX(), y - vec.getY(), z - vec.getZ());
	}
	
	public Vector3f sub(float f) {
		return this.setXYZ(x - f, y - f, z - f);
	}
	
	public Vector3f mul(Vector3f vec) {
		return this.setXYZ(x * vec.getX(), y * vec.getY(), z * vec.getZ());
	}
	
	public Vector3f mul(float f) {
		return this.setXYZ(x * f, y * f, z * f);
	}
	
	public Vector3f div(Vector3f vec) {
		return this.setXYZ(x / vec.getX(), y / vec.getY(), z / vec.getZ());
	}
	
	public Vector3f div(float f) {
		return this.setXYZ(x / f, y / f, z / f);
	}
	
	public float getX() {
		return x;
	}

	public int getXInt() {
		return (int) x;
	}
	
	public Vector3f setX(float x) {
		this.x = x;
		return this;
	}

	public float getY() {
		return y;
	}

	public int getYInt() {
		return (int) y;
	}
	
	public Vector3f setY(float y) {
		this.y = y;
		return this;
	}

	public float getZ() {
		return z;
	}

	public int getZInt() {
		return (int) z;
	}
	
	public Vector3f setZ(float z) {
		this.z = z;
		return this;
	}
	
	public String toString() {
		return "X: " + x + " Y: " + y + " Z: " + z;
	}
	
}
