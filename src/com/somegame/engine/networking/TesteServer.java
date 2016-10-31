package com.somegame.engine.networking;

public class TesteServer {
	
	private ServerInterface server;
	
	public TesteServer() {
		server = new ServerInterface();
		server.start();
	}
	
	public static void main(String[] args) {
		new TesteServer();
	}
	
}
