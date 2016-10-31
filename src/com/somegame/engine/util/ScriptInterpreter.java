package com.ioavthiago.engine.util;

/*
 * Interpretador de Scripts.
 * Deve ser usado como singleton, já que só há um arquivo data.dat (Scripts encriptados) que é
 * carregado ao inicializar (no construtor) o ScriptInterpreter.
 * A partir de então, pode-se pedir as informações de um Script com readScript(String name) 
 * pedindo pelo nome correspondente.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.ioavthiago.engine.encryption.ScriptDecrypter;
import com.ioavthiago.engine.util.datastructure.ComponentStringData;
import com.ioavthiago.engine.util.datastructure.TextFile;

public class ScriptInterpreter {

	public static ScriptInterpreter interpreter = new ScriptInterpreter();
	
	// HashMap que relaciona a extensão de cada tipo de script com um HashMap que relaciona os nomes
	// dos scripts dessa extensão com sua TextFile correspondente
	private HashMap<String, HashMap<String, TextFile>> scriptsSortedByExtension;

	
	/*
	 * Construtor
	 * 
	 * No construtor, primeiro obtém-se um HashMap com todos os scripts decriptados, depois filtramos todos
	 * eles por extensão e organizamos o HashMap scriptsSortedByExtension
	 * 
	 */
	public ScriptInterpreter() {
		HashMap<String, TextFile> scripts = ScriptDecrypter.getScripts();
		scriptsSortedByExtension = new HashMap<String, HashMap<String, TextFile>>();
		
		for (Entry<String, TextFile> e : scripts.entrySet()) {
			// Caso ainda não exista um HashMap<String, TextFile> inicializado para essa extensão, inicializar
			// primeiro
			if (!scriptsSortedByExtension.containsKey(e.getValue().getExtension())) {
				scriptsSortedByExtension.put(e.getValue().getExtension(), new HashMap<String, TextFile>());
			}
			// Adicionar os Strings e TextFiles ao HashMap<String, TextFile> correspondente
			scriptsSortedByExtension.get(e.getValue().getExtension()).put(e.getKey(), e.getValue());
		}
	}

	/*
	 * Obter um HashMap<String, TextFile> contendo todos os scripts de uma dada extensão em formato
	 * TextFile indexados por nome.
	 * 
	 * Útil para carregar shaders ou coisas que não são escritas com Components.
	 */
	public HashMap<String, TextFile> getScripts(String extension) {
		return scriptsSortedByExtension.get(extension);
	}
	
	public TextFile getScript(String extension, String name) {
		return scriptsSortedByExtension.get(extension).get(name);
	}
	
	/*
	 * Interpretar Script
	 * Busca pelo nome dado como argumento no HashMap scripts e caso encontre algo, 
	 * gera uma ArrayList de ComponentStringData contendo os dados em formato de String
	 * correspondentes a cada Component do Script.
	 */
	public ArrayList<ComponentStringData> interpretScript(String name, String extension) {
		// Resultado da função
		ArrayList<ComponentStringData> script = new ArrayList<ComponentStringData>();

		// Checar existência
		if (!scriptsSortedByExtension.get(extension).containsKey(name)) {
			System.out.println("Script nao econtrado (" + name + ").");
			return null;
		}

		// Linha atual
		String currentLine;
		// ComponentStringData a ser preenchido atualmente
		ComponentStringData currentComponent = null;
		// Acumulador de ultima linha lida para caso ';' não seja encontrado na linha atual
		String lastLine = "";

		// Ler linha a linha na variavel currentLine e tratar cada caso até o fim do TextFile
		while ((currentLine = scriptsSortedByExtension.get(extension).get(name).nextLine()) != null) {
			// Ignorar comentarios
			if (currentLine.startsWith("#") || currentLine.startsWith("//")) {
				continue;
			}
			// Inicializar novo objeto ComponentStringData (components iniciam com '_')
			if (currentLine.startsWith("_")) {
				// Quando um novo component for encontrado, devemos guardar o último gerado
				// Se esse for o primeiro, o "último" é null
				// Se não for null, guardar na lista de retorno
				if (currentComponent != null) {
					script.add(currentComponent);
				}
				// Iniciar o novo ComponentStringData com seu nome
				currentComponent = new ComponentStringData(currentLine.substring(1, currentLine.length()));
			} else { // Preencher objeto ComponentStringData com os dados propriamente ditos
				// Testar se toda a informação de um campo já está disponível (';' determina fim de um campo)
				if (currentLine.endsWith(";")) {
					// Acumular LastLine (estará vazia caso a última linha tenha sido lida completamente)
					currentLine = lastLine + currentLine;
					currentLine = currentLine.replace(";", "");					
					// Words[0] sera o nome do campo e words[1] serao os dados do campo
					String[] words = currentLine.split("=");
					// Essa lista vai no currentComponent (carregando os dados de cada campo)
					ArrayList<String> list = new ArrayList<String>();
					for (int i = 0; i < words.length; i++) {
						words[i] = words[i].trim();
					}
					
					// Tratar o caso de os dados estarem num array {...}
					if (words[1].startsWith("{")) {
						words[1] = words[1].replace("{", "").replace("}", "");
						// Contem todas os dados que estavam num array de um campo
						String[] arrayWords = words[1].split(",");
						for(String s : arrayWords) {
							list.add(s.trim());
						}
					} else {
						list.add(words[1].trim());
					}
					
					// Adicionar os dados do campo associados ao nome do campo
					currentComponent.add(words[0].trim(), list);
					
					// Esvaziar lastLine se encontrar um ';' em currentLine (linha lida completamente)
					lastLine = "";
				} else {
					// Acumular a linha atual em lastLine caso ';' não seja encontrado
					lastLine += currentLine;
				}
			}
		}
		
		script.add(currentComponent);
		
		return script;	
	}
	
}
