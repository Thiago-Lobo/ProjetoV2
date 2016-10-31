package com.ioavthiago.engine.util.datastructure;

/*
 * Retangulo
 * Abstrai a "entidade" matemática retângulo. Deve ser construído com um ponto (superior esquerdo),
 * largura e altura. Oferece getters, setters, testes de fit (em largura, altura ou área), funções
 * de divisão (cortes na horizontal ou vertical que origiam outros 2 retângulos) e teste de interseção.
 * Pode ser usado, por exemplo, como caixa de colisão de entidades.
 * 
 */

public class Rectangle {

	private int x;
	private int y;
	private int width;
	private int height;
	
	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/*
	 * Fit em largura
	 */
	public boolean fitsWidth(Rectangle r) {
		return r.getWidth() >= this.width;
	}
	
	/*
	 * Fit em altura
	 */
	public boolean fitsHeight(Rectangle r) {
		return r.getHeight() >= this.height;
	}
	
	/*
	 * Fit em área
	 */
	public boolean fits(Rectangle r) {
		return fitsWidth(r) && fitsHeight(r);
	}

	/*
	 * Fit perfeito em largura
	 */
	public boolean perfectlyFitsWidth(Rectangle r) {
		return r.getWidth() == this.width;
	}
	
	/*
	 * Fit perfeito em altura
	 */
	public boolean perfectlyFitsHeight(Rectangle r) {
		return r.getHeight() == this.height;
	}
	
	/*
	 * Fit perfeito em área
	 */
	public boolean perfectlyFits(Rectangle r) {
		return perfectlyFitsWidth(r) && perfectlyFitsHeight(r);
	}
	
	/*
	 * Dividir em dois retângulos com reta horizontal (cima/baixo).
	 * Retorna um array de retângulos. O primeiro é o de cima e o segundo é o de baixo.
	 */
	public Rectangle[] horizontalSplit(int splitHeight) {
		Rectangle[] result = new Rectangle[2];
		
		// Ret cima
		result[0] = new Rectangle(x, y, width, splitHeight);
		// Ret baixo
		result[1] = new Rectangle(x, y + splitHeight, width, height - splitHeight);
		
		return result;
	}
	
	/*
	 * Dividir em dois retângulos com reta vertical (esquerda/direita).
	 * Retorna um array de retângulos. O primeiro é o da esquerda e o segundo é o da direita.
	 */
	public Rectangle[] verticalSplit(int splitWidth) {
		Rectangle[] result = new Rectangle[2];
		
		// Ret esquerda
		result[0] = new Rectangle(x, y, splitWidth, height);
		// Ret direita
		result[1] = new Rectangle(x + splitWidth, y, width - splitWidth, height);
		
		return result;
	}
	
	/*
	 * Teste de intereseção
	 * Retorna true caso haja interseção entre este retângulo e o dado como argumento.
	 */
	public boolean intersects(Rectangle r) {
		return !(x + width - 1 < r.getX() || // r está muito a direita
				 y + height - 1 < r.getY() ||  // r está muito para baixo
				 r.getX() + r.getWidth() - 1 < x || // r está muito para a esquerda
				 r.getY() + r.getHeight() - 1 < y); // r está muito para cima
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public String toString() {
		return "Rectangle: x=" + x + " y=" + y + " w=" + width + " h=" + height;
	}
	
}
