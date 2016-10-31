package com.somegame.engine.rendering;

/*
 * SpriteBatcher
 * 
 * 
 * 
 */

import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glNormalPointer;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.util.ArrayList;

import com.somegame.engine.rendering.shader.BasicTransformShader;
import com.somegame.engine.util.Util;
import com.somegame.engine.util.datastructure.Vertex;

public class SpriteBatcher {

	private ShaderProgram shader;
	private Texture texture;
	
	private OrthographicTransform transform;
	
	private int vboHandle;
	private int iboHandle;
	private int indexCount;
	
	private ArrayList<Vertex> vertices;
	private ArrayList<Integer> indices;
	
	public SpriteBatcher() {
		vboHandle = glGenBuffers();
		iboHandle = glGenBuffers();
		
		vertices = new ArrayList<Vertex>();
		indices = new ArrayList<Integer>();
		indexCount = 0;
		
		transform = new OrthographicTransform();
		transform.setOrthographic(0, Window.getWidth(), Window.getHeight(), 0, 1f, -1f);
		
		shader = BasicTransformShader.getInstance();
		shader.bind();
	}
	
	public void render(TextureAtlas t, String area, float x, float y, float width, float height, float r, float g, float b, float a) {
		render(t, area, x, y, width, height, 0, 1f, 1f, x, y, r, g, b, a);
	}
	
	public void render(TextureAtlas t, String area, float x, float y, float width, float height, float rotation, float scaleX, float scaleY, float xAnchor, float yAnchor, float r, float g, float b, float a) {
		render(t, x, y, width, height, t.getSubTexture(area).getU1(), t.getSubTexture(area).getV1(), t.getSubTexture(area).getU2(), t.getSubTexture(area).getV2(), rotation, scaleX, scaleY, xAnchor, yAnchor, r, g, b, a);
	}
	
	public void render(Framebuffer f, float x, float y, float width, float height) {	
		render(f, x, y, width, height, 0, 1f, 1f, x, y);
	}
	
	public void render(Framebuffer f, float x, float y, float width, float height, float rotation, float scaleX, float scaleY, float xAnchor, float yAnchor) {
		render(f.getTexture(), x, y, width, height, 0f, 1f, 1f, 0f, rotation, scaleX, scaleY, xAnchor, yAnchor, 1f, 1f, 1f, 1f);
	}
	
	public void render(TextureAtlas t, String area, float x, float y, float width, float height) {
		render(t, area, x, y, width, height, 0, 1f, 1f, x, y);
	}
	
	public void render(TextureAtlas t, String area, float x, float y, float width, float height, float rotation, float scaleX, float scaleY, float xAnchor, float yAnchor) {
		render(t, x, y, width, height, t.getSubTexture(area).getU1(), t.getSubTexture(area).getV1(), t.getSubTexture(area).getU2(), t.getSubTexture(area).getV2(), rotation, scaleX, scaleY, xAnchor, yAnchor, 1f, 1f, 1f, 1f);
	}
	
	public void render(float x, float y, float width, float height, float u1, float v1, float u2, float v2) {
		render(texture, x, y, width, height, u1, v1, u2, v2);
	}
	
	public void render(float x, float y, float width, float height, float u1, float v1, float u2, float v2, float rotation, float scaleX, float scaleY, float xAnchor, float yAnchor) {
		render(texture, x, y, width, height, u1, v1, u2, v2, rotation, scaleX, scaleY, xAnchor, yAnchor, 1f, 1f, 1f, 1f);
	}
	
	public void render(Texture t, float x, float y, float width, float height, float u1, float v1, float u2, float v2) {
		render(t, x, y, width, height, u1, v1, u2, v2, 0f, 1f, 1f, x, y, 1f, 1f, 1f, 1f);
	}
	
	public void render(Texture t, float x, float y, float width, float height, float u1, float v1, float u2, float v2, float rotation,
					   float scaleX, float scaleY, float xAnchor, float yAnchor, float r, float g, float b, float a) {
		if (t != texture) {
			end();
			begin();				
			texture = t;
		}
		
		vertices.add(new Vertex().setPos((x - width/2), (y - height/2)).setRotationDeg(rotation).setAnchorPoint(xAnchor, yAnchor).setScaling(scaleX, scaleY).setUV(u1, v1).setRGB(r, g, b).setAlpha(a));
		vertices.add(new Vertex().setPos((x + width/2), (y - height/2)).setRotationDeg(rotation).setAnchorPoint(xAnchor, yAnchor).setScaling(scaleX, scaleY).setUV(u2, v1).setRGB(r, g, b).setAlpha(a));
		vertices.add(new Vertex().setPos((x + width/2), (y + height/2)).setRotationDeg(rotation).setAnchorPoint(xAnchor, yAnchor).setScaling(scaleX, scaleY).setUV(u2, v2).setRGB(r, g, b).setAlpha(a));
		vertices.add(new Vertex().setPos((x - width/2), (y + height/2)).setRotationDeg(rotation).setAnchorPoint(xAnchor, yAnchor).setScaling(scaleX, scaleY).setUV(u1, v2).setRGB(r, g, b).setAlpha(a));
		
		indices.add(0 + 4 * indexCount);
		indices.add(1 + 4 * indexCount);
		indices.add(2 + 4 * indexCount);
		indices.add(0 + 4 * indexCount);
		indices.add(2 + 4 * indexCount);
		indices.add(3 + 4 * indexCount);
		
		indexCount++;
	}
	
	public void begin() {
		indexCount = 0;
		indices.clear();
		vertices.clear();
	}
	
	public void end() {
		
		glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
		glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboHandle);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedIntBuffer(indices), GL_STATIC_DRAW);
		
		shader.updateUniforms(transform.getOrthographic());
		texture.bind();
		shader.bind();
		
		glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
		
		glVertexPointer		(2, GL_FLOAT, 	Vertex.SIZE * 4, 0L);
		glColorPointer		(4, GL_FLOAT, 	Vertex.SIZE * 4, 2 * 4);
		glTexCoordPointer	(4, GL_FLOAT, 	Vertex.SIZE * 4, 6 * 4);
		glNormalPointer		(	GL_FLOAT, 	Vertex.SIZE * 4, 10 * 4);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboHandle);
		
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);

		glDrawElements(GL_TRIANGLES, indices.size(), GL_UNSIGNED_INT, 0);
		
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
		
	}
	
	public void resize(int x, int y, int w, int h) {
		transform.setOrthographic(x, w, h, y, 1f, -1f);
	}
	
	public void resize() {
		transform.setOrthographic(0, Window.getWidth(), Window.getHeight(), 0, 1f, -1f);
	}
	
	public void setAnchorPoint(float x, float y) {
		transform.setAnchorPoint(x, y);
	}
	
	public void setTranslation(float x, float y) {		
		transform.setTranslation(x, y);
	}
	
	public void setRotation(float z) {
		transform.setRotation(z);
	}
	
	public void setScaling(float x, float y) {
		transform.setScaling(x, y);
	}
	
	public void setTexture(Texture t) {
		texture = t;
	}
	
}
