package com.somegame.engine.rendering;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window {

	private static boolean sync = true;
	
	public static void createWindow(int width, int height, String title, boolean vSync) {
				
		Display.setTitle(title);
		Display.setResizable(true);
		
		try {			
			Display.setDisplayMode(new DisplayMode(width, height));
			
			Display.create();
			Keyboard.create();
			Mouse.create();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		setVSync(vSync);
		
	}
	
	public static void setFullscreen(boolean fullscreen) {
		try {
			Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setVSync(boolean vSync) {
		Display.setVSyncEnabled(vSync);
		sync = vSync;		
	}
	
	public static void setTitle(String title) {
		Display.setTitle(title);
	}
	
	public static void dispose() {
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
	
	public static void update() {
		Display.update();
		if (sync) {
			Display.sync(60);			
		}
	}
	
	public static boolean wasResized() {
		return Display.wasResized();
	}
	
	public static boolean isCloseRequested() {
		return Display.isCloseRequested();
	}
	
	public static int getWidth() {
		return Display.getWidth();
	}
	
	public static int getHeight() {
		return Display.getHeight();
	}
	
	public static String getTitle() {
		return Display.getTitle();
	}
	
}
