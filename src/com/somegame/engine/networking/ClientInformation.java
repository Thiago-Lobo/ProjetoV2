package com.somegame.engine.networking;

import java.net.InetAddress;
import java.util.ArrayList;

public class ClientInformation {

	private InetAddress ip;
	private int port;
	private int id;
	private String name;
	private ArrayList<Command> lastServerToClient;
	private ArrayList<Command> lastClientToServer;
	private ArrayList<Command> reliableBuffer;
	
	public ClientInformation(InetAddress ip_, int port_, String name_) {
		ip = ip_;
		port = port_;
		name = name_;
	}
	
	public InetAddress getIp() {
		return ip;
	}
	
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name_) {
		name = name_;
	}

	public ArrayList<Command> getLastServerToClient() {
		return lastServerToClient;
	}

	public void setLastServerToClient(ArrayList<Command> lastServerToClient) {
		this.lastServerToClient = lastServerToClient;
	}

	public ArrayList<Command> getLastClientToServer() {
		return lastClientToServer;
	}

	public void setLastClientToServer(ArrayList<Command> lastClientToServer) {
		this.lastClientToServer = lastClientToServer;
	}
	
}
