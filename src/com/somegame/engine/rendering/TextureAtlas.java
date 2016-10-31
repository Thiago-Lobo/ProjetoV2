package com.somegame.engine.rendering;

import java.util.HashMap;

import com.somegame.engine.util.ScriptInterpreter;
import com.somegame.engine.util.datastructure.TextFile;

public class TextureAtlas extends Texture {

	private HashMap<String, TextureArea> textures;
	
	public TextureAtlas(String fileName) {
		super(fileName);
		textures = new HashMap<String, TextureArea>();
		initialize(fileName);
	}
	
	private void initialize(String fileName) {
		TextFile tf = ScriptInterpreter.interpreter.getScript("rect", fileName);
		String currentLine = "";
		
		while((currentLine = tf.nextLine()) != null) {
			String[] tokens = currentLine.split("\\s+");
			textures.put(tokens[0], new TextureArea(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]), Float.parseFloat(tokens[4])));
		}
	}
	
	public TextureArea getSubTexture(String name) {
		return textures.get(name);
	}
	
}
