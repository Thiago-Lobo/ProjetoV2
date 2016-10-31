package com.ioavthiago.engine.util.datastructure;

import java.util.HashMap;

/*
 * Nó de uma árvore binária de particionamento espacial.
 * É representado por um retângulo e uma variável do tipo T (o que está guardado no retângulo).
 * A variável isFull representa que o nó está alocado (object != null).
 * Left e right são os nós que derivam do nó atual.
 * 
 * LEFT E RIGHT, NO CONTEXTO DOS NÓS, NÃO TEM RELAÇÃO AOS RETÂNGULOS GERADOS APÓS DIVISÕES! SÃO APENAS
 * NOMES DE VARIÁVEIS QUE FACILITAM DESENHAR A ÁRVORE! (left pode ser cima ou esquerda)
 * 
 * Oferece getters, setters, funções para divisão horizontal ou vertical (gera os nós left e right com
 * as partes correspondentes do rectangle).
 * 
 * Deve ser construido com um Retângulo.
 */

public class SpatialTreeNode<T> {

	private boolean isFull;
	private Rectangle rectangle;
	private SpatialTreeNode<T> left;
	private SpatialTreeNode<T> right;
	private T object;
	
	public SpatialTreeNode(Rectangle r) {
		left = null;
		right = null;
		isFull = false;
		rectangle = r;
	}
	
	/*
	 * Alocar o nó atual com um objeto dado como argumento.
	 */
	private void allocate(T object) {
		this.object = object;
		this.isFull = true;
	}
	
	/*
	 * Adicionar um objeto ao nó, e, consequentemente, à árvore.
	 * A adição é feita de forma recursiva e retorna true caso o objeto seja alocado com sucesso e false
	 * caso contrário.
	 * A adição sempre começa pelo nó da esquerda, e caso não seja possível, tenta o da direita.
	 */
	public boolean put(Rectangle r, T object) {		
		
		// Caso o nó da esquerda seja null (ainda não criado)
		if (left == null) {
			// Se o retângulo (espaço ocupado pelo objeto atual) cabe nesse nó (o atual, não o da esquerda ou direita!)
			// e se o nó atual não está ocupado
			if (!isFull && r.fits(rectangle)) {
				// Se o o objeto encaixa perfeitamente no nó atual não precisa subdividir, basta alocar
				if (r.perfectlyFits(rectangle)) {
					allocate(object);					
				} else { // Se não, precisa dividir
					// Por convenção, dividimos na maior dimensão do retângulo a ser adicionado.
					// Se a largura é maior que a altura (dividiremos na largura, ou seja, horizontalmente).
					if (r.getWidth() > r.getHeight()) {
						// Dividir horizontalmente
						horizontalSplit(r.getHeight());
						// Após a divisão, se o retângulo a ser adicionado NÃO cabe perfeitamente no
						// nó da esquerda que acabou de ser gerado, devemos dividir esse nó em dois
						// para que na esquerda fique o objeto alocado e na direita o espaço restante
						if (!r.perfectlyFits(left.getRectangle())) {
							left.verticalSplit(r.getWidth());
						}
					} else { // Fazer o mesmo de cima, no caso da altura ser maior que a largura
						verticalSplit(r.getWidth());
						if (!r.perfectlyFits(left.getRectangle())) {
							left.horizontalSplit(r.getHeight());
						}
					} // Se o nó da esquerda tiver sido subdividido (ou seja, o objeto cabe nele e sobra espaço)
					if (left.getLeft() != null) {
						// Alocar no nó da esquerda do nó da esquerda do nó atual (calma, eu sei que é confuso.)
						left.getLeft().allocate(object);											
					} else { // Caso contrário, o objeto cabe perfeitamente no nó da esquerda do nó atual (ele não foi subdividido)
						// E basta alocar
						left.allocate(object);											
					}
				}
				// Em todos esses casos, a alocação funcionou
				return true;
			} else {
				// O else da primeira condição implica que o objeto nunca será alocado nesse nó
				return false;
			}
		} else { // Aqui é feita a recursão (isso roda caso o nó da esquerda do nó atual não seja null [já subdividiu antes]).
			// Essa boolean guarda a informação: "Deu pra alocar na esquerda do nó da esquerda do nó atual"
			boolean okLeft = left.put(r, object);
			// Essa boolean guarda a informação: "Deu pra alocar na direita do nó da esquerda (sim, esquerda) do nó atual"
			boolean okRight = false;
			
			// Se, numa mesma execução, não tiver dado pra alocar no nó da esquerda do nó da esquerda do nó atual, tentar no da direita.
			if (!okLeft) {
				okRight = right.put(r, object);			
			}
			
			// Retornar se em algum lugar deu pra alocar
			return okRight || okLeft;
			
			// Pense no que está aqui em cima recursivamente. Funciona!!
		}
		
	}
	
	/*
	 * Navegar pela árvore e printar a informação de cada nó. (Supõe que os objects têm toString()).
	 */
	public void print() {
		if (left != null) {
			left.print();			
		}
		if (isFull) {
			System.out.println(object + " " + rectangle);
		}
		if (right != null) {
			right.print();			
		}
	}
	
	/*
	 * Recolher a informação da árvore num HashMap que associa objetos do tipo T aos retângulos dos
	 * nós correspondentes.
	 */
	public void collectInfo(HashMap<T, Rectangle> collection) {
		if (left != null) {
			left.collectInfo(collection);			
		}
		if (isFull) {
			collection.put(object, rectangle);
		}
		if (right != null) {
			right.collectInfo(collection);
		}
	}
	
	/*
	 * Dividir horizontalmente
	 */
	public void horizontalSplit(int splitHeight) {
		Rectangle[] rectangles = rectangle.horizontalSplit(splitHeight);
		left = new SpatialTreeNode<T>(rectangles[0]);
		right = new SpatialTreeNode<T>(rectangles[1]);
	}

	/*
	 * Dividir verticalmente
	 */
	public void verticalSplit(int splitWidth) {
		Rectangle[] rectangles = rectangle.verticalSplit(splitWidth);
		left = new SpatialTreeNode<T>(rectangles[0]);
		right = new SpatialTreeNode<T>(rectangles[1]);
	}
	
	public boolean isFull() {
		return isFull;
	}

	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public SpatialTreeNode<T> getLeft() {
		return left;
	}

	public void setLeft(SpatialTreeNode<T> left) {
		this.left = left;
	}

	public SpatialTreeNode<T> getRight() {
		return right;
	}

	public void setRight(SpatialTreeNode<T> right) {
		this.right = right;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}
	
}
