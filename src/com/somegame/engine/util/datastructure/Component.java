package com.ioavthiago.engine.util.datastructure;

/*
 * Componente
 * Elemento básico para criação de entidades.
 * Servem como pacotes de dados referentes a certa parte da lógica do jogo. São descritos nos
 * Scripts.
 * Components só devem conter dados e lógica de acesso. Todo o processamento dos dados fica
 * delegado aos Sistemas.
 */

import java.util.HashMap;

public class Component {
	// Associador de nomes de components (name) a inteiros.
	// Esses inteiros são os indices do int[] contido em cada entidade.
	// Serve para automatizar a criação de components através de Scripts.
	// Ao obter o nome de um Component num script, busca-se seu indice nesse HashMap e utiliza-se
	// tal indice para selecionar a DataPool correspondente e também a posição correspondente no
	// int[] de uma entidade.
	public static HashMap<String, Integer> componentIndexes = new HashMap<String, Integer>();
	public static int componentCount = 0;
	protected String componentName;
	
	/*
	 * Aqui fica a lógica para associar cada component ao seu ID numérico no momento de criação.
	 * Obviamente não se pode associar certo component a um ID caso isso já tenha sido feito.
	 */
	public Component(String name) {
		this.componentName = name.toUpperCase();
		if (!componentIndexes.containsKey(name.toUpperCase())) {
			componentIndexes.put(name.toUpperCase(), componentCount++);
		}		
	}
	
	public String getComponentName() {
		return componentName;
	}
	
	public Component getCopy() {
		return null;
	}
	
	public Component getCopy(ComponentStringData componentStringData) {
		return null;
	}
		
	public String toString() {
		return null;
	}
	
}
