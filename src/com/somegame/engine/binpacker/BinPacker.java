package com.somegame.engine.binpacker;

/*
 * Bin Packer
 * Essa clase contém função main, por isso deve ser utilizada como um programa individual.
 * Serve para gerar uma SpriteSheet e um arquivo de texto (.rect) com as informações pertinentes a cada uma
 * das Texturas que foram agrupadas na SpriteSheet (nome e coordenadas uv).
 * 
 * O programa usa uma SpatialTree para fazer a lógica de alocação.
 */

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import com.somegame.engine.util.datastructure.Rectangle;
import com.somegame.engine.util.datastructure.SpatialTree;

public class BinPacker {

	// SpatialTree para alocação das imagens espacialmente
	private SpatialTree<String> tree;
	// Formato permitido
	private final String ext = "png";

	public BinPacker() {
		// Inicializa a SpriteSheet com 1024 x 1024 pixels (convenção)
		tree = new SpatialTree<String>(1024, 1024);
	}

	// Inicializar
	public void init(String path) {
		// Obter um HashMap<String, BufferedImage> relacionando todas as imagens encontradas no path dado
		HashMap<String, BufferedImage> imageList = generateImageList(path);
		// Gerar a SpriteSheet final e o arquivo de texto correspondente
		buildSpriteSheetAndTextFile(imageList, path);		
	}

	
	/*
	 * Construir SpriteSheet e arquivo de texto
	 * Recebe uma lista de imagens indexada pelo nome e um caminho.
	 * Gera, no caminho, uma SpriteSheet com todas as imagens (se houver espaço) contidas em imageList
	 * e junto a um arquivo de texto (.rect) com as informações de cada Textura alocada (nome e coordenadas uv)
	 * A lógica é preencher uma árvore binária de particionamento espacial com retângulos do tamanho de cada
	 * uma das imagens que contêm o nome da imagem correspondente.
	 * Depois, percorre-se todas as entradas da árvore e utiliza-se a String (nome da imagem) relacionada
	 * a cada retângulo, para, junto das coordenadas e lartura/altura de cada retângulo, copiar os pixels
	 * de cada Textura para a SpriteSheet.
	 * 
	 * Pode-se aplicar um offset (padrão de 2 pixels) para evitar problemas de amostragem.
	 */
	private void buildSpriteSheetAndTextFile(HashMap<String, BufferedImage> imageList, String path) {
		// Essa é a imagem final
		BufferedImage result = new BufferedImage(tree.getWidth(), tree.getHeight(), BufferedImage.TYPE_INT_ARGB);
		// Essa é a string que acumula o texto que vai para o arquivo de texto
		String textFile = "";
		// Offset a ser aplicado na alocação
		int offset = 2;
		
		// Para cada imagem em imageList, adicionar um novo retângulo à árvore binária considerando o offset
		for (Entry<String, BufferedImage> e : imageList.entrySet()) {
			tree.add(new Rectangle(0, 0, (e.getValue().getWidth() + offset), (e.getValue().getHeight()) + offset), e.getKey());
		}
		
		// Obter as entradas da árvore
		HashMap<String, Rectangle> treeInfo = tree.getInfo();
		
		// Percorrer as entradas da árvore
		for (Entry<String, Rectangle> e : treeInfo.entrySet()) {
			// Obter a imagem atual pela String de cada entrada
			BufferedImage currentImage = imageList.get(e.getKey());
			// Obter a localização espacial (retângulo) pelo Rectangle de cada entrada
			Rectangle location = e.getValue();
			// Aqui ficarão os pixels da Textura atual (simples cópia de pixels)
			int[] currentRGB = currentImage.getRGB(0, 0, currentImage.getWidth(), currentImage.getHeight(), null, 0, currentImage.getWidth());
			// Colocar na imagem final (SpriteSheet) os pixels copiados anteriormente considerando o offset
			result.setRGB(location.getX() + offset/2, location.getY() + offset/2, location.getWidth() - offset, location.getHeight() - offset, currentRGB, 0, location.getWidth() - offset);
			
			// Escrever as coordenadas normalizadas (uv) de cada Textura no arquivo de texto
			float x1 = ((float) (e.getValue().getX() + offset/2)/tree.getWidth());
			float y1 = ((float) (e.getValue().getY() + offset/2)/tree.getHeight());
			float x2 = ((float) (e.getValue().getX() + e.getValue().getWidth() - offset/2)/tree.getWidth());
			float y2 = ((float) (e.getValue().getY() + e.getValue().getHeight() - offset/2)/tree.getHeight());
			// Nome x1 y1 x2 y2
			textFile = textFile + String.format(Locale.ENGLISH, e.getKey() + " %.10f %.10f %.10f %.10f\r\n", x1, y1, x2, y2);
		}
		
		try {
			// Escrever arquivo de texto
			File file = new File(path + "spritesheet.rect"); 
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(textFile);
			bw.close();
			
			// Escrever imagem no disco
			File outputfile = new File(path + "spritesheet.png");
			ImageIO.write(result, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Obter a lista de imagens png num dado path.
	 * Retorna um HashMap<String, BufferedImage> relacionando o nome de cada imagem à própria imagem.
	 */
	private HashMap<String, BufferedImage> generateImageList(String path) {
		// Lista de arquivos que interessam
		ArrayList<File> filesList = new ArrayList<File>();
		// Variável de retorno
		HashMap<String, BufferedImage> imageList = new HashMap<String, BufferedImage>();
		// Array de arquivos em path
		File[] files = new File(path).listFiles();

		// Obter apenas os .png dentro de files
		for (int i = 0; i < files.length; i++) {
			int j = files[i].getName().lastIndexOf(".");
			String extension = "";

			if (j > 0) {
				extension = files[i].getName().substring(j + 1);
			}

			// Testar a extensão de cada arquivo e se o nome contem a palavra spritesheet (nesse caso será ignorada)
			if (extension.equals(ext) && !files[i].getName().contains("spritesheet")) {
				// Guardar na lista de arquivos que interessam
				filesList.add(files[i]);
			}
		}

		// Percorrer cada um dos arquivos e adicionar entradas a imageList removendo a extensão
		try {
			for (File f : filesList) {
				imageList.put(f.getName().replace(".png", ""), ImageIO.read(f));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return imageList;
	}

	/*
	 * Main
	 * Instancia um BinPacker e o inicializa. Por padrão, busca pela pasta ./res/
	 * Um outro caminho pode ser dado como argumento.
	 */
	public static void main(String[] args) {
		BinPacker packer = new BinPacker();
		if (args.length != 0) {
			packer.init(args[0]);
		} else {
			packer.init("./res/");
		}
	}

}
