package com.ioavthiago.engine.util.datastructure;

public class Matrix4f {
	
	private float[][] m;
	
	public Matrix4f() {
		m = new float[4][4];
	}
	
	public Matrix4f initIdentity() {
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initPerspective(float fov, float width, float height, float zNear, float zFar) {
		float tanHalfFOV = (float)Math.tan(Math.toRadians(fov / 2));
		float ar = width/height;
		float zRange = zNear - zFar;
		
		m[0][0] = 1.0f / (tanHalfFOV * ar);	m[0][1] = 0;					m[0][2] = 0;						m[0][3] = 0;
		m[1][0] = 0;						m[1][1] = 1.0f / tanHalfFOV;	m[1][2] = 0;						m[1][3] = 0;
		m[2][0] = 0;						m[2][1] = 0;					m[2][2] = (-zNear - zFar)/zRange;	m[2][3] = 2 * zFar * zNear / zRange;
		m[3][0] = 0;						m[3][1] = 0;					m[3][2] = 1;						m[3][3] = 0;
		
		return this;
	}
	
	public Matrix4f initOrthographic(float right, float left, float top, float bottom, float zFar, float zNear) {
		
		m[0][0] = 2.0f / (right - left);	m[0][1] = 0;						m[0][2] = 0;						m[0][3] = -(float)(right + left) / (right - left);
		m[1][0] = 0;						m[1][1] = 2.0f / (top - bottom);	m[1][2] = 0;						m[1][3] = -(float)(top + bottom) / (top - bottom);
		m[2][0] = 0;						m[2][1] = 0;						m[2][2] = -2.0f / (zFar - zNear);	m[2][3] = -(float)(zFar + zNear) / (zFar - zNear);
		m[3][0] = 0;						m[3][1] = 0;						m[3][2] = 0;						m[3][3] = 1;
		
		return this;		
	}
	
	public Matrix4f initTranslation(float x, float y, float z) {
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = x;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = y;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = z;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initScaling(float x, float y, float z) {
		m[0][0] = x;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = y;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = z;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initRotation(float x, float y, float z) {
		Matrix4f rotX = new Matrix4f();
		Matrix4f rotY = new Matrix4f();
		Matrix4f rotZ = new Matrix4f();
		
		x = (float) Math.toRadians(x);
		y = (float) Math.toRadians(y);
		z = (float) Math.toRadians(z);
		
		rotZ.m[0][0] = (float)Math.cos(z);	rotZ.m[0][1] = -(float)Math.sin(z);	rotZ.m[0][2] = 0;	rotZ.m[0][3] = 0;
		rotZ.m[1][0] = (float)Math.sin(z);	rotZ.m[1][1] =  (float)Math.cos(z);	rotZ.m[1][2] = 0;	rotZ.m[1][3] = 0;
		rotZ.m[2][0] = 0;					rotZ.m[2][1] = 0;					rotZ.m[2][2] = 1;	rotZ.m[2][3] = 0;
		rotZ.m[3][0] = 0;					rotZ.m[3][1] = 0;					rotZ.m[3][2] = 0;	rotZ.m[3][3] = 1;
		
		rotX.m[0][0] = 1;	rotX.m[0][1] = 0;					rotX.m[0][2] = 0;					rotX.m[0][3] = 0;
		rotX.m[1][0] = 0;	rotX.m[1][1] = (float)Math.cos(x);	rotX.m[1][2] = -(float)Math.sin(x); rotX.m[1][3] = 0;
		rotX.m[2][0] = 0;	rotX.m[2][1] = (float)Math.sin(x);	rotX.m[2][2] =  (float)Math.cos(x);	rotX.m[2][3] = 0;
		rotX.m[3][0] = 0;	rotX.m[3][1] = 0;					rotX.m[3][2] = 0;					rotX.m[3][3] = 1;
		
		rotY.m[0][0] = (float)Math.cos(y);	rotY.m[0][1] = 0;	rotY.m[0][2] = -(float)Math.sin(y);	rotY.m[0][3] = 0;
		rotY.m[1][0] = 0;					rotY.m[1][1] = 1;	rotY.m[1][2] = 0;					rotY.m[1][3] = 0;
		rotY.m[2][0] = (float)Math.sin(y);	rotY.m[2][1] = 0;	rotY.m[2][2] =  (float)Math.cos(y);	rotY.m[2][3] = 0;
		rotY.m[3][0] = 0;					rotY.m[3][1] = 0;	rotY.m[3][2] = 0;					rotY.m[3][3] = 1;
		
		m = rotZ.mul(rotY.mul(rotX)).getM();
		return this;
	}
	
	public Matrix4f initCamera(Vector3f forward, Vector3f up) {
		Vector3f f = forward.normalize();
		Vector3f r = up.cross(f).normalize();
		Vector3f u = f.cross(r);
		
		m[0][0] = r.getX();	m[0][1] = r.getY();	m[0][2] = r.getZ();	m[0][3] = 0;
		m[1][0] = u.getX();	m[1][1] = u.getY();	m[1][2] = u.getZ();	m[1][3] = 0;
		m[2][0] = f.getX();	m[2][1] = f.getY();	m[2][2] = f.getZ();	m[2][3] = 0;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f clone() {
		return new Matrix4f().setM(this.getM());
	}
	
	public Matrix4f mul(Matrix4f r) {
		Matrix4f result = new Matrix4f();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				result.set(i, j, m[i][0] * r.get(0, j) +
						  m[i][1] * r.get(1, j) +
						  m[i][2] * r.get(2, j) +
						  m[i][3] * r.get(3, j));
			}
		}
		
		return result;
	}
	
	public float[][] getM() {
		return m;
	}

	public Matrix4f setM(float[][] m) {
		this.m = m;
		return this;
	}
	
	public float get(int x, int y) {
		return m[x][y];
	}
	
	public Matrix4f set(int x, int y, float val) {
		m[x][y] = val;
		return this;
	}
	
	public String toString() {
		return m[0][0] + " " + m[0][1] + " " + m[0][2] + " " + m[0][3] + "\n" +
			   m[1][0] + " " + m[1][1] + " " + m[1][2] + " " + m[1][3] + "\n" +
			   m[2][0] + " " + m[2][1] + " " + m[2][2] + " " + m[2][3] + "\n" +
			   m[3][0] + " " + m[3][1] + " " + m[3][2] + " " + m[3][3];
	}

}
