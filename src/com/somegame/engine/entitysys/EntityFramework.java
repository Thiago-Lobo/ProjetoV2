package com.somegame.engine.entitysys;

/*
 * Framework de Entidades da Engine.
 * 
 * Classe principal da engine. Guarda todas as entidades e sistemas ativos e permite que sistemas acessem
 * entidades.
 * 
 * Aplica o modelo Component-System:
 * Uma entidade é representada por um conjunto de Components, pacotes de dados que ~não executam nenhuma lógica~
 * do jogo e apenas guardam informações. O processamento desses dados acontece inteiramente nos sistemas, sendo
 * cada um responsável por uma parte da lógica do jogo, acessando apenas determinados Components.
 * 
 * Uma entidade é representada, no contexto do EntityFramework, como um ID numérico associado a um array de int
 * cada coluna no array de int está associada a uma ComponentList e o valor guardado em cada coluna do array de
 * int representa o endereço do Component em questão dentro da ComponentList em questão. Se o valor for -1, a entidade
 * não possui tal Component.
 * 
 * Exemplo: só existem duas ComponentList (dois Components diferentes, consequentemente). A primeira, associada a 0, 
 * 			é a de NameComponent e a segunda, associada a 1, é a de NumberComponent.
 * 			Uma certa entidade possui seu array de int da seguinte forma: {-1, 3}
 * 			A interpretação correta é: na coluna 0, temos endereço -1, logo, a entidade não possui NameComponent e
 * 			na coluna 1, o endereço é 3, logo, o NumberComponent dessa entidade está na posição 3 dentro da ComponentList
 * 			de NumberComponent.
 * 
 * O EntityFramework também guarda as instâncias individuais de cada uma das ComponentLists, de forma publica, para que
 * os sistemas possam acessá-las livremente, sem qualquer TypeCasting.
 * 
 * Sempre que desejarmos criar uma entidade, devemos usar a função addEntity(String name) passando o nome do Script dessa
 * entidade como argumento. Como toda entidade vem de um Script, que é composto apenas por Strings, utilizamos "Entidades Exemplo" 
 * para evitar interpretar Strings exaustivamente, sempre que uma entidade for criada. Na primeira vez que um certo nome
 * de entidade é pedido, sua Entidade Exemplo é gerada pela função initializeExampleEntity. A partir daí, sempre que formos
 * criar outra entidade desse tipo, a função addEntity tratará de copiar todos os dados iniciais (como no script) da Entidade Exemplo
 * para a nova entidade. Isso evita a reinterpretação de Strings.
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;

import com.somegame.engine.entitysys.system.LogicSystem;
import com.somegame.engine.util.ScriptInterpreter;
import com.somegame.engine.util.datastructure.Component;
import com.somegame.engine.util.datastructure.ComponentList;
import com.somegame.engine.util.datastructure.ComponentStringData;
import com.somegame.engine.util.datastructure.component.NameComponent;
import com.somegame.engine.util.datastructure.component.NumberComponent;

public class EntityFramework {
	
	// Entidades carregadas
	private HashMap<Integer, int[]> entities;
	// Entidades exemplo carregadas
	private HashMap<String, int[]> exampleEntities;
	// Lista de sistemas para tick
	private ArrayList<LogicSystem> systems;
	// Referencia direta ao sistema de renderizacao. Para tick direto
	// (frequencia diferente de tick e render).
	private LogicSystem renderSystem;
	// Contagem de entidades para ajudar na geracao de ids.
	private int entityCount;

	// Instancias de cada uma das ComponentLists
	public ComponentList<NameComponent> nameComponentList = new ComponentList<NameComponent>(NameComponent.class);
	public ComponentList<NumberComponent> numberComponentList = new ComponentList<NumberComponent>(NumberComponent.class);
	
	public EntityFramework() {
		entities = new HashMap<Integer, int[]>();
		exampleEntities = new HashMap<String, int[]>();
		entityCount = 0;
	}
	
	/*
	 * Inicializar Entidade Exemplo
	 * 
	 * Obtém os dados do Script pedido em 'name' e cria e armazena (em ComponentLists) os Components gerados após a interpretação
	 * de Strings feita pela função getCopy().
	 * 
	 */
	private void initializeExampleEntity(String name) {
		// Dados do script
		ArrayList<ComponentStringData> scriptData = ScriptInterpreter.interpreter.interpretScript(name, "ent");
		// Array de endereços da nova entidade
		int[] newEntity = new int[Component.componentCount];
	
		// Começar supondo que a nova entidade não possui nenhum Component
		for(int i = 0; i < newEntity.length; i++) {
			newEntity[i] = -1;
		}
		
		// Para cada ComponentStringData
		for(int i = 0; i < scriptData.size(); i++) {
			// Tratar cada tipo separadamente
			switch (scriptData.get(i).getName().toUpperCase()) {
								// Criar o Component com getCopy, interpretando o ComponentStringData atual
				case "NAME": 	NameComponent nameComponent = NameComponent.example.getCopy(scriptData.get(i));
								// Armazenar o Component criado na ComponentList correspondente e guardar o endereço obtido no array de endereços da entidade exemplo
								newEntity[Component.componentIndexes.get("NAME")] = nameComponentList.add(nameComponent);
								break;
				case "NUMBER": 	NumberComponent numberComponent = NumberComponent.example.getCopy(scriptData.get(i));
								newEntity[Component.componentIndexes.get("NUMBER")] = numberComponentList.add(numberComponent);
								break;
			}
		}
		
		// Guardar a entidade exemplo associada ao nome de seu Script
		exampleEntities.put(name, newEntity);		
	}

	// Atualizar os sistemas
	public void tick() {
		for (LogicSystem s : systems) {
			s.tick();
		}
	}

	// Atualizar o sistema de renderização
	public void render() {
		renderSystem.tick();
	}

	// Adicionar sistemas
	public void setSystems(ArrayList<LogicSystem> systems) {
		this.systems = systems;
	}

	// Adicionar sistema de renderização
	public void setRenderSystem(LogicSystem renderSystem) {
		this.renderSystem = renderSystem;
	}

	// Obter entidade a partir de ID numérico
	public int[] getEntity(int id) {
		return entities.get(id);
	}

	// Remover entidade a partir de ID numérico
	public void removeEntity(int id) {
		entities.remove(id);
	}

	/*
	 * Criar uma entidade
	 * 
	 * Tenta criar uma entidade em 'entities' a partir do nome de um Script (.ent). 
	 * Caso essa entidade nunca tenha sido criada, inicializa uma versão exemplo.
	 * 
	 */
	public int addEntity(String name) {
		// Gerar novo ID
		int targetId = entityCount;

		// Atualizar ID
		entityCount++;
		
		// Checar se exemplo já foi criada antes
		if (!exampleEntities.containsKey(name)) {
			// Se não, criar exemplo
			initializeExampleEntity(name);
		}

		// Obter endereços de exemplo
		int[] reference = exampleEntities.get(name);
		// Endereços da nova entidade ficarão aqui
		int[] components = new int[Component.componentCount];
		
		// Começar supondo que a nova entidade não possui nenhum Component
		for(int i = 0; i < components.length; i++) {
			components[i] = -1;
		}
		
		// Percorrer o array de endereços
		for (int i = 0; i < components.length; i++) {
			// Caso a referência possua o Component correspondente à coluna i
			if (reference[i] != -1) {
				// Guardar no array na nova entidade o endereço obtido ao copiar os dados do Component referência
				// para o um novo Component, o qual é armazenado na mesma ComponentList
				components[i] = ComponentList.componentLists.get(i).copy(reference[i]);
			}
		}
		
		// Armazenar a entidade criada
		entities.put(targetId, components);

		// Retornar o índice
		return targetId;
	}

	// Main para testes
	public static void main(String[] args) {
		EntityFramework ef = new EntityFramework();
		ef.addEntity("b");
		ef.addEntity("a");
		System.out.println(ef.nameComponentList);
		System.out.println(ef.numberComponentList);
	}
	
}
