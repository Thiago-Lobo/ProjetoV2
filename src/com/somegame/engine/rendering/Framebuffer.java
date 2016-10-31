package com.somegame.engine.rendering;

/*
 * Framebuffer Object
 * 
 * Wrapper para o Framebuffer Object oferecido pelo OpenGL. Seve para substituir o buffer padrão (tela) na hora
 * de renderizar. Qualquer renderização realizada num Framebuffer fica em sua textura, a qual pode ser acessada
 * com um getter.
 * 
 * Opcionalmente, pode-se criar um FrameBuffer com informação de profundidade (depth). Não é necessário para 2D
 * em geral. Deve-se informar a largura e a altura em tempo de criação.
 * 
 * Indica-se guardar os Framebuffers no HashMap framebuffers, associados a um certo nome. Não é ideal recriar
 * Framebuffers que são usados em todo loop do jogo. Isso adiciona overhead e é inútil.
 * 
 * O uso de um Framebuffer consiste em executar código de renderização entre uma chamada de begin() e outra de
 * end(). Depois disso, pode-se usar getTexture() para obter o que foi renderizado anteriormente.
 * 
 */

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class Framebuffer {

	// HashMap para instâncias de Framebuffers
	public static HashMap<String, Framebuffer> framebuffers = new HashMap<String, Framebuffer>();

	// Nome do Framebuffer
	private String name;

	// Textura na qual renderiza-se
	private Texture texture;

	// Informação para OpenGL - handles e presença de depthBuffer
	private int framebufferHandle;
	private int depthbufferHandle;
	private boolean hasDepth;

	// Largura e altura
	private int width;
	private int height;

	public Framebuffer(int w, int h, boolean depth) {
		width = w;
		height = h;
		hasDepth = depth;
		build();
	}
	
	public Framebuffer(String n, int w, int h, boolean depth) {
		this(w, h, depth);
		name = n;
	}
	
	// Aqui o framebuffer é efetivamente criado
	private void build() {
		// Inicializar a textura completamente vazia (null)
		texture = new Texture(width, height, (ByteBuffer) null);
		
		// Obter um ponteiro para o frameBuffer
		framebufferHandle = glGenFramebuffersEXT();
		

		// Caso seja necessário um depthBuffer
		if (hasDepth) {
			// Obter ponteiro para o depthBuffer
			depthbufferHandle = glGenRenderbuffersEXT();

			// Iniciar configuração do depthBuffer
			glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthbufferHandle);

			// Setar width e height
			glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT, width, height);
			
			// Acabar configuração do depthBuffer
			glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, 0);
		}
		
		// Iniciar configuração do frameBuffer
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferHandle);

		// Associar a textura criada ao frameBuffer
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, texture.getHandle(), 0);
		
		// Se desejado, associar o depthBuffer ao frameBuffer
		if (hasDepth) {
			glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, depthbufferHandle);
		}

		// Acabar a configuração do frameBuffer
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

		// Debug...
		int status = glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT);

		if (status != GL_FRAMEBUFFER_COMPLETE_EXT) {
			System.err.println("Error: Framebuffer Object initialization gone wrong.");
			System.exit(1);
		}

	}

	/*
	 * Begin
	 * 
	 * Prepara o contexto para utilizar o frameBuffer
	 */
	public void begin() {
		// Ativar o frameBuffer
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferHandle);
		// Mudar a glViewPort
		GLControlPanel.resize(0, 0, width, height);
		// Setar a cor para limpar a tela
		GLControlPanel.setClearColor(0f, 0f, 0f, 0f);
		// Limpar a tela
		GLControlPanel.clearScreen();
	}
	
	// Begin especificando a cor para limpar a tela
	public void begin(int r, int g, int b, int a) {
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferHandle);
		GLControlPanel.resize(0, 0, width, height);
		GLControlPanel.setClearColor(r, g, b, a);
		GLControlPanel.clearScreen();
	}
	
	/*
	 * End
	 * 
	 * Retorna ao contexto ativo antes de begin()
	 */
	public void end() {
		// Desliga o frameBuffer
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		// Seta a cor de limpar a tela padrão
		GLControlPanel.setDefaultClearColor();
		// Seta a viewPort padrão
		setDefaultViewPort();
	}

	// End especificando a nova viewPort
	public void end(int x, int y, int w, int h) {
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		GLControlPanel.setDefaultClearColor();
		GLControlPanel.resize(x, y, w, h);
	}

	private void setDefaultViewPort() {
		GLControlPanel.resize(0, 0, Window.getWidth(), Window.getHeight());
	}

	// Dispose - liberar recursos alocados na placa de vídeo
	public void dispose() {
		texture.dispose();
		glDeleteFramebuffersEXT(framebufferHandle);
		
		if (hasDepth) {
			glDeleteRenderbuffersEXT(depthbufferHandle);
		}
		
		if (framebuffers.containsKey(name)) {
			framebuffers.remove(name);
		}
	}

	public void enableAlphaCorrection() {
		
	}
	
	/*
	 * Put
	 * 
	 * Funcão estática pra adicionar um frameBuffer ao HashMap geral de frameBuffers
	 */
	public static void put(String n, Framebuffer framebuffer) {
		framebuffer.setName(n);
		framebuffers.put(n, framebuffer);
	}
	
	// Getters e Setters
	public void setName(String n) {
		name = n;
	}
	
	public Texture getTexture() { return texture; }
	
	public String getName() { return name; }
	
	public int getWidth() { return width; }
	
	public int getHeight() { return height; }
	
}
