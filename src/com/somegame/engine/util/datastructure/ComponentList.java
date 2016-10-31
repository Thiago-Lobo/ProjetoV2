package com.ioavthiago.engine.util.datastructure;

import java.util.ArrayList;
import java.util.HashMap;

public class ComponentList<T extends Component> {

	public static HashMap<Integer, ComponentList<? extends Component>> componentLists = new HashMap<Integer, ComponentList<? extends Component>>();

	private ArrayList<T> objects;
	private ArrayList<Integer> nullPositions;
	private int id;
	
	public ComponentList(Class<T> clazz) {
		objects = new ArrayList<T>();
		nullPositions = new ArrayList<Integer>();
		id = -1;
		
		try {
			T instance = clazz.newInstance();
			if (id == -1) {
				id = Component.componentIndexes.get(instance.getComponentName());
				componentLists.put(id, this);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public T get(int index) {
		return objects.get(index);
	}
	
	public void remove(int index) {
		objects.set(index, null);
		nullPositions.add(index);
	}
	
	@SuppressWarnings("unchecked")
	public int copy(int from) {
		int to = objects.size();
		
		if (nullPositions.size() != 0) {
			to = nullPositions.get(0);
			nullPositions.remove(0);
			objects.set(to, (T) objects.get(from).getCopy());
			return to;
		}
		
		objects.add((T) objects.get(from).getCopy());
		return to;
	}
	
	public int add(T object) {
		int index = objects.size();
		
		if (nullPositions.size() != 0) {
			index = nullPositions.get(0);
			nullPositions.remove(0);
			objects.set(index, object);
			return index;
		}
				
		objects.add(object);
		return index; 
	}
	
	public String toString() {
		return "ComponentList - Allocations: " + objects.size() + " Free: " + nullPositions.size();
	}
	
}
