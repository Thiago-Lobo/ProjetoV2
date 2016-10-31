package com.ioavthiago.engine.util;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import com.ioavthiago.engine.util.datastructure.Matrix4f;
import com.ioavthiago.engine.util.datastructure.Vertex;

public class Util {

	public static FloatBuffer createFloatBuffer(int size) {
		return BufferUtils.createFloatBuffer(size);
	}

	public static IntBuffer createIntBuffer(int size) {
		return BufferUtils.createIntBuffer(size);
	}

	public static ByteBuffer createByteBuffer(int size) {
		return BufferUtils.createByteBuffer(size);
	}

	public static ByteBuffer createFlippedBuffer(BufferedImage image) {
		ByteBuffer buffer = createByteBuffer(image.getWidth() * image.getHeight() * 4);
		
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[x + y * image.getWidth()];
				buffer.put((byte) ((pixel >> 16) & 0xFF));    
	            buffer.put((byte) ((pixel >> 8) & 0xFF));     
	            buffer.put((byte) (pixel & 0xFF));            
	            buffer.put((byte) ((pixel >> 24) & 0xFF));    
			}
		}
		
		buffer.flip();
		
		return buffer;	
	}
	
	public static FloatBuffer createFlippedBuffer(Matrix4f value) {
		FloatBuffer buffer = createFloatBuffer(4 * 4);

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				buffer.put(value.get(i, j));
			}
		}

		buffer.flip();

		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(ArrayList<Vertex> vertices) {
		FloatBuffer buffer = createFloatBuffer(vertices.size() * Vertex.SIZE);
	
		for (Vertex vertex : vertices) {
			buffer.put(vertex.getPos().getX());
			buffer.put(vertex.getPos().getY());
			buffer.put(vertex.getRGB().getX());
			buffer.put(vertex.getRGB().getY());
			buffer.put(vertex.getRGB().getZ());
			buffer.put(vertex.getAlpha());
			buffer.put(vertex.getUV().getX());
			buffer.put(vertex.getUV().getY());
			buffer.put(vertex.getScaling().getX());
			buffer.put(vertex.getScaling().getY());
			buffer.put(vertex.getRotation());
			buffer.put(vertex.getAnchorPoint().getX());
			buffer.put(vertex.getAnchorPoint().getY());
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static IntBuffer createFlippedIntBuffer(ArrayList<Integer> values) {
		IntBuffer buffer = createIntBuffer(values.size());
	
		for (Integer i : values) {
			buffer.put(i);
		}
		
		buffer.flip();
		
		return buffer;		
	}
	
}
