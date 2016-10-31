package com.somegame.engine.util;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.somegame.engine.rendering.Framebuffer;
import com.somegame.engine.rendering.GLControlPanel;
import com.somegame.engine.rendering.SpriteBatcher;
import com.somegame.engine.rendering.Texture;
import com.somegame.engine.rendering.TextureAtlas;
import com.somegame.engine.rendering.Window;

public class TestClass {

	public static void main(String[] args) {
		
		Window.createWindow(1276, 756, "Testando", false);	
		
		GLControlPanel.initGraphics();
		System.out.println("testando");
		TextureAtlas texture = new TextureAtlas("spritesheet");
		SpriteBatcher batch = new SpriteBatcher();
		batch.setTexture(texture);
		Timer fps = new Timer(1);
		fps.start();
		int frames = 0;
		float fpsAccumulator = 0;
		float seconds = 0;
		
		Framebuffer.put("teste", new Framebuffer(512, 512, false));
		Framebuffer teste = Framebuffer.framebuffers.get("teste");
		
		teste.begin();
		batch.begin();
		batch.resize(0, 0, teste.getWidth(), teste.getHeight());
		batch.render(texture, "bricks", 256, 256, 512, 512);
		batch.end();
		teste.end();
		
		while(!(Window.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))) {
			GLControlPanel.clearScreen();	
			
			batch.begin();
			batch.resize();
			batch.setAnchorPoint(Window.getWidth()/2, Window.getHeight()/2);
			batch.render(texture, "grass", 256, 256, 512, 512);
			batch.render(teste, 512, 256, 512, 512);
			batch.render(teste, 512, 256, 256, 256);
			batch.render(texture, "white", 500, 500, 100, 100, 0, 1, 1, 500, 500, 1, 0, 0, 1f);
			batch.render(texture, "white", 100, 100, 8, 8, 0, 0, 1, 1);
			batch.end();
			
			frames++;
			
			if (fps.timePassed()) {
				System.out.println("FPS: " + frames);
				fpsAccumulator += frames;
				seconds++;
				Window.setTitle("Testando - FPS: " + frames);
				frames = 0;
			}
			
			if (Window.wasResized()) {
				batch.resize();
				GLControlPanel.resize(0, 0, Window.getWidth(), Window.getHeight());
			}
			
			Window.update();		
		}
		
		System.out.println("Average FPS: " + fpsAccumulator/seconds);
		Window.dispose();
		System.exit(0);
		
	}
	
}