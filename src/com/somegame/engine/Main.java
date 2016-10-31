package com.somegame.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.somegame.engine.entitysys.EntityFramework;

public class Main {

	private EntityFramework framework;
	
	public Main() {
		//rodar programa ao iniciar
		try {
			String s = null;
			Process q = Runtime.getRuntime().exec("java -jar ./res/app.jar ./res/");
			//Process q = Runtime.getRuntime().exec("java -jar app.jar");
			BufferedReader stdOutput = new BufferedReader(new InputStreamReader(q.getInputStream()));
			while ((s = stdOutput.readLine()) != null) {
                System.out.println(s);
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		Main main = new Main();
	}
	
}