package com.somegame.engine.entitysys.system;

/*
 * Classe base para qualquer Sistema.
 * 
 * Implementa o código básico de cada Sistema:
 * Atualizar, adicionar/remover entidade, acesso ao nome.
 * No construtor, implementa-se a lógica de associação de nome com ID numérico no HashMap
 * systemIndexes. Esse HashMap pode ser usado em acessos futuros à lista de sistemas do 
 * EntityFramework.
 * 
 * Todo LogicSystem possui um ArrayList<Integer>[][] que representa a indexação espacial das entidades
 * que estão cadastradas no sistema. Serve para encontrar as entidades por sua posição de forma mais
 * rápida.
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;

import com.somegame.engine.entitysys.EntityFramework;

public class LogicSystem {

	public static HashMap<String, Integer> systemIndexes = new HashMap<String, Integer>();
	public static int systemCount = 0;
	protected String systemName;
	
	protected EntityFramework framework;
	// Lista geral de entidades
	private ArrayList<Integer> entities;
	// Indexação espacial de entidades
	private ArrayList<Integer>[][] entitiesInTiles;
	// Talvez criar uma nova lista aqui, contendo só as entidades que que não possuem localização.
	
	public LogicSystem(EntityFramework framework, String name) {
		this.systemName = name;
		this.framework = framework;
		// Aqui prevenimos que um nome seja "cadastrado" mais que uma vez.
		// Isso não faria sentido...
		if (!systemIndexes.containsKey(name)) {
			systemIndexes.put(name, systemCount++);
		}		
	}
	
	public String getSystemName() {
		return systemName;
	}
	
	public void tick() {
		
	}
	
	public void addEntity(int id) {
		entities.add(id);
	}
	
	public void removeEntity(int id) {
		entities.remove((Object) id);
	}
	
}
