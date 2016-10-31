package com.ioavthiago.engine.util.datastructure;

import java.util.HashMap;

/*
 * Árvore de particionamento espacial binária
 * Deve ser criada para um certo tipo de objeto.
 * Possui largura e altura (do nó inicial, que representa a raíz da árvore).
 * Possui o nó raíz.
 * Oferece setters e getters, funções para adicionar, obter informações e printar a árvore.
 * Todas as operaçÕes diferentes de getters e setters são implementadas, em grande parte, na classe
 * SpatialRootNode, já que a árvore é simplesmente representada pelo nó inicial.
 * Sim, pode-se fazer uma árvore igual só criando o nó inicial como um objeto individual.
 */

public class SpatialTree<T> {

	private SpatialTreeNode<T> root;
	private int width;
	private int height;
	
	public SpatialTree(int width, int height) {
		this.width = width;
		this.height = height;
		root = new SpatialTreeNode<T>(new Rectangle(0, 0, width, height));
	}
	
	public void add(Rectangle r, T object) {
		boolean success = root.put(r, object);		
		if (!success) {
			System.out.println("Arvore cheia.");
		}
	}
	
	public void print() {
		root.print();
	}
	
	public HashMap<T, Rectangle> getInfo() {
		HashMap<T, Rectangle> result = new HashMap<T, Rectangle>();
		root.collectInfo(result);
		return result;
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
	
}
