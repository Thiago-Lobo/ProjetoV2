package com.somegame.engine.encryption;

/*
 * Decriptador de Scripts.
 * 
 * Deve ser usado de forma estática, ao iniciar a engine, pela função getScripts. 
 * Busca o arquivo /res/data.dat e retorna um HashMap relacionando o nome de cada 
 * Script com seu texto.
 * 
 */

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.somegame.engine.util.datastructure.TextFile;

public class ScriptDecrypter {
	
	/*
	 * Decriptar os dados no arquivo data.dat e retornar a String correspondente. 
	 */
	private static String decryptData() {		
		File file = new File("./res/data.dat");
		byte[] data = null;
		String result = "";
		
		try {
			RandomAccessFile RAMFile = new RandomAccessFile(file, "r");
			int length = (int) RAMFile.length();
			data = new byte[length];
			RAMFile.readFully(data);
			RAMFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (data != null) {
			String key = "1234567890987654";
			byte[] raw = key.getBytes(Charset.forName("US-ASCII"));
			SecretKeySpec sKeySpec = new SecretKeySpec(raw, "AES");
			byte[] original = null;
			try {
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, sKeySpec, new IvParameterSpec(new byte[16]));
				original = cipher.doFinal(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (original != null) {
				result = new String(original, Charset.forName("US-ASCII"));
			}
		}
		
		return result;
	}
	
	/*
	 * Interpretar a String retornada por decryptData().
	 * 
	 * Supoe-se que a String contenha o texto de todos os Scripts das extensões que interessam.
	 * Nas primeiras linhas do arquivo (header), devem estar relacionados os nomes dos Scripts
	 * e a "linha" dentro da String onde deve-se iniciar a leitura para obter cada Script.
	 * Primeiro, o header é colocado num HashMap, o qual é posteriormente utilizado para
	 * gerar TextFiles com o texto de cada Script.
	 * 
	 */	
	public static HashMap<String, TextFile> getScripts() {
		// String decodificada "crua"
		String unsorted = decryptData();
		// Retorno da funcao
		HashMap<String, TextFile> result = new HashMap<String, TextFile>();
		
		// Testar arquivo vazio
		if (unsorted.length() != 0) {
			// HashMap que representa o header
			HashMap<Integer, String> namesAndLines = new HashMap<Integer, String>();
			// Dividir a String em linhas
			String[] tokens = unsorted.split("\r\n");
			
			// Gerar um ArrayList de linhas
			ArrayList<String> lines = new ArrayList<String>();
			String currentLine = "";
			
			for (String s : tokens) {
				lines.add(s);
			}
						
			// Gerar um TextFile que representa o arquivo que unifica os Scripts
			TextFile scripts = new TextFile(lines);
			
			// Ler linha a linha até encontrar o fim do header (linha vazia)
			while ((currentLine = scripts.nextLine()).length() != 0) {
				// Dividir cada linha do header em palavras
				String[] subTokens = currentLine.split("\\s+");
				String name = "";
				int lineNum = 0;
				
				// Obter o nome de um Script e sua linha no arquivo principal
				for (int i = 0; i < subTokens.length; i++) {
					if (i < subTokens.length - 1) {
						name += subTokens[i] + " ";
					} else {
						lineNum = Integer.parseInt(subTokens[i]);
					}
				}
				// Guardar informação no HashMap
				namesAndLines.put(lineNum, name.trim());
			}
			
			// Obter e ordenar chaves do HashMap do header (linhas iniciais de Scripts)
			ArrayList<Integer> keys = new ArrayList<Integer>();
			for (Integer i : namesAndLines.keySet()) {
				keys.add(i);
			}
			Collections.sort(keys);
			
			// Acessar o arquivo principal pelas linhas em "keys"
			for (int i = 0; i < keys.size(); i++) {
				// Pular para a linha inicial
				scripts.seek(keys.get(i));
				// Aqui ficarão as linhas lidas
				ArrayList<String> subLines = new ArrayList<String>();
				// Ler até o início do próximo Script (ou fim do arquivo)
				try {
					for (int lineNum = keys.get(i); lineNum < keys.get(i + 1); lineNum++) {
						subLines.add(scripts.nextLine());
					}
				} catch (Exception e) {
					for (int lineNum = keys.get(i); lineNum < scripts.getSize(); lineNum++) {
						subLines.add(scripts.nextLine());
					}
				}
				
				// Separar o nome absoluto do arquivo em nome e extensão
				String fileName = namesAndLines.get(keys.get(i));
				int dotIndex = fileName.indexOf(".");
				String name = fileName.substring(0, dotIndex);
				String extension = fileName.substring(dotIndex + 1, fileName.length());
				
				// Gerar TextFile correspondente
				TextFile subFile = new TextFile(subLines);
				// Guardar a extensão do TextFile
				subFile.setExtension(extension);
				// Guardar no HashMap de retorno relacionado ao nome
				result.put(name, subFile);
			}			
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		getScripts();
	}
	
}
