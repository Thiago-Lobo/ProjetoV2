package com.somegame.engine.rendering;

/*
 * Transformação Ortográfica
 * 
 * Essa classe gera uma Matriz de Projeção Ortográfica transformada para ser dada aos VertexShaders. A transformação
 * é calculada a partir dos parâmetros da tela.
 * 
 * Para ter a origem no canto superior esquerdo, +y para baixo e +x para a direita utilizar:
 * Left = 0
 * Right = Largura
 * Bottom = Altura
 * Top = 0
 * Near = 1
 * Far = -1
 * 
 */


import com.somegame.engine.util.datastructure.Matrix4f;
import com.somegame.engine.util.datastructure.Vector2f;

public class OrthographicTransform {
	
	// Parâmetros da matriz Ortográfica
	private float left;
	private float right;
	private float bottom;
	private float top;
	private float near;
	private float far;
	
	// Vetores de Transformação
	private Vector2f anchorPoint;
	private Vector2f translation;
	private Vector2f scaling;
	private float rotation;
	
	// Matrizes de Transformação
	private Matrix4f translationMatrix;
	private Matrix4f rotationMatrix;
	private Matrix4f scalingMatrix;
	private Matrix4f anchorTranslationMatrix;
	private Matrix4f inverseAnchorTranslationMatrix;
	
	// Matriz Ortográfica
	private Matrix4f projection;
	
	public OrthographicTransform() {
		anchorPoint = new Vector2f().setXY(0f, 0f);
		translation = new Vector2f().setXY(0f, 0f);
		scaling = new Vector2f().setXY(1f, 1f);
		
		translationMatrix = new Matrix4f().initIdentity();
		rotationMatrix = new Matrix4f().initIdentity();
		scalingMatrix = new Matrix4f().initIdentity();
		anchorTranslationMatrix = new Matrix4f().initIdentity();
		inverseAnchorTranslationMatrix = new Matrix4f().initIdentity();
		
		projection = new Matrix4f().initIdentity();
	}
	
	/*
	 * GetTransformation
	 * 
	 * Retorna a Matriz de Transformação calculada.
	 */
	private Matrix4f getTransformation() {
		// Obter matrizes de Transformação
		translationMatrix.initTranslation(translation.getX(), translation.getY(), 0f);
		rotationMatrix.initRotation(0f, 0f, rotation);
		scalingMatrix.initScaling(scaling.getX(), scaling.getY(), 1f);
		anchorTranslationMatrix.initTranslation(-anchorPoint.getX(), -anchorPoint.getY(), 0f);
		inverseAnchorTranslationMatrix.initTranslation(anchorPoint.getX(), anchorPoint.getY(), 0f);
		
		// Multiplicá-las
		return inverseAnchorTranslationMatrix.mul(translationMatrix.mul(rotationMatrix.mul(scalingMatrix.mul(anchorTranslationMatrix))));
	}
	
	// Setar os parâmetros da Projeção
	public void setOrthographic(float l, float r, float b, float t, float n, float f) {
		left = l;
		right = r;
		bottom = b;
		top = t;
		near = n;
		far = f;
	}
	
	/*
	 * GetOrhographic
	 * 
	 * Obter a Matriz de Projeção calculada e transformada.
	 */
	public Matrix4f getOrthographic() {
		Matrix4f transformationMatrix = getTransformation();
		Matrix4f projectionMatrix = projection.initOrthographic(right, left, top, bottom, far, near);
		
		return projectionMatrix.mul(transformationMatrix);
		//return projectionMatrix;
	}
	
	// Setters de parâmetros de transformação
	public OrthographicTransform setTranslation(float x, float y) {
		translation.setXY(x, y);
		return this;
	}
	
	public OrthographicTransform setRotation(float z) {
		rotation = z;
		return this;
	}
	
	public OrthographicTransform setScaling(float x, float y) {
		scaling.setXY(x, y);
		return this;
	}
	
	public OrthographicTransform setAnchorPoint(float x, float y) {
		anchorPoint.setXY(x, y);
		return this;
	}
	
}
