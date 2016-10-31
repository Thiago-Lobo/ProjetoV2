package com.somegame.engine.rendering;

/*
 * ShaderProgram
 * 
 * Wrapper para Shaders. Todo Shader criado deve extender essa classe.
 * Oferece funções para criação do shader como: setar o texto do Vertex Shader, setar o texto do Pixel Shader,
 * compilar, setar Uniforms e transferir Uniforms.
 *
 * Todo Shader deve ser 'binded' antes da renderização para ser usado.
 * 
 */

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;

import com.somegame.engine.util.Util;
import com.somegame.engine.util.datastructure.Matrix4f;

public class ShaderProgram {

	// Endereço na placa de vídeo
	private int handle;
	// HashMap das Uniforms
	private HashMap<String, Integer> uniforms;
	
	public ShaderProgram() {
		handle = glCreateProgram();
		uniforms = new HashMap<String, Integer>();
		
		if (handle == 0) {
			System.err.println("Shader creation failed: Could not find valid memory location in constructor.");
			System.exit(1);
		}
	}
	
	// Função para atualizar as uniforms
	public void updateUniforms(Matrix4f projectedMatrix) {
		
	}

	// Ativar o Shader
	public void bind() {
		glUseProgram(handle);
	}
	
	// Adicionar Uniform
	public void addUniform(String uniform) {
		int uniformLocation = glGetUniformLocation(handle, uniform);
		
		if (uniformLocation == 0xffffff) {
			System.err.println("Error: Could not find uniform (ID: " + uniform + ") inside GPU memory.");
			System.exit(1);
		}
		uniforms.put(uniform, uniformLocation);
	}
	
	// Adicionar Vertex Shader
	public void addVertexShader(String text) {
		addProgram(text, GL_VERTEX_SHADER);
	}
	
	// Adicionar Fragment Shader
	public void addFragmentShader(String text) {
		addProgram(text, GL_FRAGMENT_SHADER);
	}
	
	// Compilar Shader
	public void compileShader() {
		glLinkProgram(handle);
		
		if (glGetProgrami(handle, GL_LINK_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(handle, 1024));
			System.exit(1);
		}
		
		glValidateProgram(handle);
		
		if (glGetProgrami(handle, GL_VALIDATE_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(handle, 1024));
			System.exit(1);
		}
	}
	
	private void addProgram(String text, int type) {
		int shader = glCreateShader(type);
		
		if (shader == 0) {
			System.err.println("Shader creation failed: could not find valid memory location while adding shader.");
		}
		
		glShaderSource(shader, text);
		glCompileShader(shader);
		
		if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}
	
		glAttachShader(handle, shader);	
	}
	
	// Setters de Uniforms
	public void setUniformi(String uniformName, int value) {
		glUniform1i(uniforms.get(uniformName), value);
	}
	
	public void setUniformf(String uniformName, float value) {
		glUniform1f(uniforms.get(uniformName), value);
	}
	 
	public void setUniform(String uniformName, float x, float y, float z) {
		glUniform3f(uniforms.get(uniformName), x, y, z);
	}
	
	public void setUniform(String uniformName, Matrix4f value) {
		glUniformMatrix4(uniforms.get(uniformName), true, Util.createFlippedBuffer(value));
	}
	
}
