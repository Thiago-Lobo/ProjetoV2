package com.ioavthiago.engine.util.datastructure.component;

import com.ioavthiago.engine.util.datastructure.Component;
import com.ioavthiago.engine.util.datastructure.ComponentStringData;

public class NumberComponent extends Component {

	public static NumberComponent example = new NumberComponent(); 
	
	private int number;
	
	public NumberComponent() {
		super("Number");
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int n) {
		number = n;
	}

	public NumberComponent getCopy() {
		NumberComponent nc = new NumberComponent();
		nc.setNumber(number);
		return nc;
	}
	
	public NumberComponent getCopy(ComponentStringData componentStringData) {
		NumberComponent nc = new NumberComponent();
		nc.setNumber(Integer.parseInt(componentStringData.getDataArray("Number").get(0)));
		return nc;
	}
	
	public String toString() {
		return Integer.toString(number);
	}
	
}
