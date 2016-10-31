package com.ioavthiago.engine.util.datastructure;

/*
 * Dados de um Component de um Script em forma de Strings.
 * Guarda o nome de um Component e seus campos.
 * Cada campo é implementado por um nome e uma lista de Strings.
 * Caso só haja um valor no campo, a lista só terá um valor.
 * Deve ser parseado na função getCopy de Components. 
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ComponentStringData {
	
	private String name;
	private HashMap<String, ArrayList<String>> data;
	
	public ComponentStringData(String name) {
		this.name = name;
		this.data = new HashMap<String, ArrayList<String>>();
	}

	public String getName() {
		return name;
	}

	public void add(String name, ArrayList<String> list) {
		data.put(name, list);
	}
	
	public ArrayList<String> getDataArray(String name) {
		return data.get(name);
	}
	
	public String toString() {
		String result = "";
		result = result + name + "\n";
		for (Entry<String, ArrayList<String>> e : data.entrySet()) {
			result = result + e.getKey() + ": ";
			for (String s : e.getValue()) {
				result = result + s + " ";
			}
			result = result + "\n";
		}
		return result;
	}
	
}
