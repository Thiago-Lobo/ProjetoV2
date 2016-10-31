package com.ioavthiago.engine.util.datastructure.component;

import com.ioavthiago.engine.util.datastructure.Component;
import com.ioavthiago.engine.util.datastructure.ComponentStringData;

/*
 * Component de Nome
 * Guarda o nome de uma entidade. Bastante auto-explicativo.
 */

public class NameComponent extends Component {

	public static NameComponent example = new NameComponent(); 
	private String name;
	
	public NameComponent() {
		super("Name");
	}

	public String getEntityName() {
		return name;
	}

	public void setEntityName(String name) {
		this.name = name;
	}

	public NameComponent getCopy() {
		NameComponent nc = new NameComponent();
		nc.setEntityName(this.name);
		return nc;
	}
	
	public NameComponent getCopy(ComponentStringData componentStringData) {
		NameComponent nc = new NameComponent();
		nc.setEntityName(componentStringData.getDataArray("Name").get(0));
		return nc;
	}
	
	public String toString() {
		return name;
	}
	
}
