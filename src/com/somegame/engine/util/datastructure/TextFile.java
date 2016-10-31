package com.ioavthiago.engine.util.datastructure;

/*
 * Arquivo Texto
 * Representa um arquivo de texto na memória do computador.
 * Deve ser utilizado para tratar arquivos de texto que encontram-se em apenas uma String, 
 * no processo de decriptação, por exemplo.
 * Mimetiza a forma de trabalhar com arquivos texto, com seek (para apontar para certa linha)
 * e nextLine(), para retornar a linha atual e ir para a próxima.
 * Deve ser inicializado com um ArrayList<String> contendo todas as linhas do arquivo texto,
 * sem '\r\n'.
 */

import java.util.ArrayList;

public class TextFile {

	private ArrayList<String> lines;
	private int linePointer;
	private String extension;
	
	public TextFile() {
		lines = new ArrayList<String>();
		linePointer = 0;
	}
	
	public TextFile(ArrayList<String> lines) {
		this();
		setLines(lines);		
	}
	
	public void setLines(ArrayList<String> lines) {
		this.lines = lines;
	}
	
	public void prepare() {
		linePointer = 0;
	}
	
	public void seek(int line) {
		linePointer = line;
	}
	
	public String nextLine() {
		if (linePointer < lines.size()) {
			return lines.get(linePointer++);
		}
		return null;		
	}
	
	public int getSize() {
		return lines.size();
	}
	
	public String toString() {
		String result = "";
		
		for (String s : lines) {
			result = result + s + "\r\n";
		}
		
		return result;		
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}	
}
