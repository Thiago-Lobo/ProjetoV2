package com.somegame.engine.encryption;

/*
 * Encriptador de Scripts.
 * Deve ser usado como programa (.jar) separado. Pode receber como argumento de linha de 
 * comando um endereço. Buscará arquivos Script (extensões listadas em extension) no endereço 
 * dado ou, caso nenhum argumento seja dado, buscara a partir do diretório atual (./).
 * Os Scripts são unificados numa String que é então encriptada e escrita em forma de bytes
 * num arquivo .dat. 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ScriptEncrypter {

	// Lista de extensões permitidas
	private final String[] extensions;
	
	public ScriptEncrypter() {
		extensions = new String[] {"ent", "rect"};
	}
	
	/*
	 * Inicializar.
	 * Coordena as tarefas do encriptador:
	 * 1) Gerar a String principal
	 * 2) Obter os bytes encriptados
	 * 3) Escrever os bytes no arquivo .dat
	 * 4) Testar decriptação e escrever o arquivo no Terminal
	 * 
	 */
	public void init(String path) {
		String scripts = formatString(path);
		System.out.println(scripts);
		if (scripts.length() != 0) {
			byte[] bytes = encrypt("1234567890987654", scripts);
			writeBytes(path, bytes);
			decrypt(path);
		}
	}

	/*
	 * Decriptar
	 * Busca o arquivo data.dat no path dado como argumento e retorna a String decriptada
	 * correspondente.
	 */
	private void decrypt(String path) {
		File file = new File(path + "data.dat");
		byte[] data = null;
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
				System.out.println(new String(original, Charset.forName("US-ASCII")));
			}
		}
	}

	/*
	 * Escrever arquivo binário com os bytes encriptados.
	 */
	private void writeBytes(String path, byte[] bytes) {
		try {
			FileOutputStream fos = new FileOutputStream(path + "data.dat");
			fos.write(bytes);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Encriptar.
	 * Gera um vetor de bytes correspondente aos dados numa String dada como argumento.
	 */
	private byte[] encrypt(String key, String value) {
		byte[] raw = key.getBytes(Charset.forName("US-ASCII"));
		SecretKeySpec sKeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = null;

		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, new IvParameterSpec(new byte[16]));
			return cipher.doFinal(value.getBytes(Charset.forName("US-ASCII")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * Formatar Scripts em uma String.
	 * Busca no path dado por arquivos Script e os organiza numa String principal 
	 * que possui um header (nome de cada Script e a linha inicial correspondente)
	 * e um corpo (texto dos Scripts propriamente ditos).
	 */
	private String formatString(String path) {
		// Obter lista de arquivos no diretório path
		ArrayList<File> filesList = new ArrayList<File>();
		File[] files = new File(path).listFiles();
		// Variável de retorno (os dados serão gravados aqui)
		String result = "";
		// Associa o nome de cada Script com sua linha inicial no corpo da String (para o header)
		HashMap<String, Integer> namesLines = new HashMap<String, Integer>();
		// Contagem de linhas para lógica da linha inicial
		int lineCount = 0;

		// Buscar arquivos com extensão que esteja dentro de extensions
		for (int i = 0; i < files.length; i++) {
			int j = files[i].getName().lastIndexOf(".");
			String extension = "";

			if (j > 0) {
				extension = files[i].getName().substring(j + 1);
			}

			// Adicionar a filesList caso a extensão seja válida
			for (String s : extensions) {
				if (extension.equals(s)) {
					filesList.add(files[i]);
				}
			}
		}

		// Lógica aplicada a cada Script encontrado
		for (File f : filesList) {
			BufferedReader br = null;

			try {
				// Contador de linha especial (se aplica apenas ao Script atual)
				int currentCount = 0;
				br = new BufferedReader(new FileReader(f));
				String currentLine = "";

				// Acumular linhas em result e incrementar a contagem especial
				while ((currentLine = br.readLine()) != null) {
					result += currentLine + "\r\n";
					currentCount++;
				}

				// Guardar o nome do arquivo associado a contagem GERAL de linhas
				namesLines.put(f.getName(), lineCount);
				// Atualizar a contagem GERAL com a espcial
				lineCount += currentCount;

				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Inicializar o header
		String header = "";
		// Percorrer entradas no HashMap do header
		for (Entry<String, Integer> e : namesLines.entrySet()) {
			// Escrever cada entrada no formato "nome número" levando em conta a mudança
			// que deve ser aplicada nos valores de linha inicial (devido ao tamanho do header)
			header = header + e.getKey() + " " + (e.getValue() + namesLines.size() + 1) + "\r\n";
		}
		
		// Adicionar o header antes do corpo
		result = header + "\r\n" + result;

		return result;
	}

	public static void main(String[] args) {
		ScriptEncrypter encrypter = new ScriptEncrypter();
		if (args.length != 0) {
			encrypter.init(args[0]);
		} else {
			encrypter.init("./res/");
		}
	}

}
