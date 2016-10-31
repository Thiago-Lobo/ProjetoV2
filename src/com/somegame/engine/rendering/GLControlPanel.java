package com.somegame.engine.rendering;

/*
 * GLControlPanel
 * 
 * Guarda o contexto atual do OpenGL. Informações como cor para limpar a tela, enable de textura, inicialização
 * do contexto, equações para blending e outras podem ser setadas estaticamente a partir dessa classe.
 * 
 */

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.EXTBlendFuncSeparate.*;

public class GLControlPanel {
	
	// Wrapper das constantes referentes às equações de blending do OpenGL. Para facilitar.
	public static final int ZERO = GL_ZERO;
	public static final int ONE = GL_ONE;
	public static final int DST_COLOR = GL_DST_COLOR;	
	public static final int ONE_MINUS_DST_COLOR = GL_ONE_MINUS_DST_COLOR;
	public static final int SRC_ALPHA = GL_SRC_ALPHA;
	public static final int ONE_MINUS_SRC_ALPHA = GL_ONE_MINUS_SRC_ALPHA;
	public static final int DST_ALPHA = GL_DST_ALPHA;
	public static final int ONE_MINUS_DST_ALPHA = GL_ONE_MINUS_DST_ALPHA;
	public static final int CONSTANT_COLOR = GL_CONSTANT_COLOR;
	public static final int ONE_MINUS_CONSTANT_COLOR = GL_ONE_MINUS_CONSTANT_COLOR;
	public static final int CONSTANT_ALPHA = GL_CONSTANT_ALPHA;
	public static final int ONE_MINUS_CONSTANT_ALPHA = GL_ONE_MINUS_CONSTANT_ALPHA;
	public static final int SRC_ALPHA_SATURATE = GL_SRC_ALPHA_SATURATE;
	
	public static void clearScreen() {
		glClear(GL_COLOR_BUFFER_BIT);
	}
	
	public static void setTexture2D(boolean enable) {
		if (enable) {
			glEnable(GL_TEXTURE_2D);
		} else {
			glDisable(GL_TEXTURE_2D);
		}
	}
	
	public static void resize(int x, int y, int width, int height) {
		glViewport(x, y, width, height);
	}
	
	public static void setDefaultClearColor() {
		glClearColor(0f, 0f, 0f, 0f);
	}
	
	public static void setClearColor(float r, float g, float b, float a) {
		glClearColor(r, g, b, a);
	}
	
	public static void initGraphics() {
		glClearColor(0f, 0f, 0f, 0f);
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void setBlendFuncSeparate(int sRGB, int dRGB, int sA, int dA) {
		glBlendFuncSeparateEXT(sRGB, dRGB, sA, dA);
	}
	
	public static void setBlendFuncSeparate() {
		glBlendFuncSeparateEXT(SRC_ALPHA, ONE_MINUS_SRC_ALPHA, ONE, ONE);
	}
	
	public static void setBlendFunc(int sFactor, int dFactor) {
		glBlendFunc(sFactor, dFactor);
	}
	
	public static void setBlendFunc() {
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static String getGLVersion() {
		return glGetString(GL_VERSION);
	}
	
	/*
	 * Função debugTexture
	 * 
	 * Essencialmente desenha uma textura na tela nas coordenadas especificadas com largura e altura especificadas.
	 * Serve para debugar uma textura e também o contexto do OpenGL.
	 * 
	 * Não deve ser usada para renderizar o jogo propriamente dito! Immediate Mode é muito lerdo!
	 * 
	 */
	public static void debugTexture(Texture tex, float x, float y, float width, float height) {
		glMatrixMode(GL_PROJECTION);
	    glLoadIdentity();
	    glOrtho(0, Window.getWidth(), Window.getHeight(), 0, 1, -1);
	    glMatrixMode(GL_MODELVIEW);
	    glLoadIdentity();
	    glEnable(GL_TEXTURE_2D);

	    tex.bind();

	    float u = 0f;
	    float v = 0f;
	    float u2 = 1f;
	    float v2 = 1f;

	    glColor4f(1f, 1f, 1f, 1f);
	    glBegin(GL_QUADS);
	        glTexCoord2f(u, v);
	        glVertex2f(x, y);
	        glTexCoord2f(u2, v);
	        glVertex2f(x + width, y);
	        glTexCoord2f(u2, v2);
	        glVertex2f(x + width, y + height);
	        glTexCoord2f(u, v2);
	        glVertex2f(x, y + height);
	    glEnd();
	}
	
}
