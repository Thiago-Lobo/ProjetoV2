package com.somegame.engine.networking;

public class TesteClient {

	private Client client;
	
	public TesteClient() {
		
		client = new Client("localhost");
		client.start();
		
		client.sendData("10%5%1%0$pituxa2769$batata".getBytes());
		client.sendData("10%5%1%1$pituxa2769$batata".getBytes());
		
		System.exit(0);
		
	}
	
	public static void main(String[] args) {
		new TesteClient();
	}
	
}
