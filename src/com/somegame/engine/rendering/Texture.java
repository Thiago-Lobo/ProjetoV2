package com.somegame.engine.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.somegame.engine.util.Util;

public class Texture {

	private int handle;
	private int width;
	private int height;
	
	public Texture(String fileName) {
		try {
			BufferedImage image = ImageIO.read(new File("./res/" + fileName + ".png"));
			this.width = image.getWidth();
			this.height = image.getHeight();
			initialize(Util.createFlippedBuffer(image));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Texture(int width, int height, ByteBuffer data) {
		this.width = width;
		this.height = height;
		initialize(data);
	}
	
	public void initialize(ByteBuffer data) {
		handle = glGenTextures();
		
		bind();
		glBindTexture(GL_TEXTURE_2D, handle);
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) data);
        unbind();
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, handle);
	}
	
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public int getHandle() {
		return handle;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void dispose() {
		if (handle != 0) {
			glDeleteTextures(handle);		
		}
	}
	
}
