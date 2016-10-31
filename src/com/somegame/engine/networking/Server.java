package com.somegame.engine.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class Server extends Thread {

	private DatagramSocket socket;
	private int count = 0;
	public String lastData;
	public ArrayList<String> buffer;
	
	public Server() {
		buffer = new ArrayList<String>();
		try {
			socket = new DatagramSocket(6789);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < 1024; i++) {
				buffer.append(String.format("%02X", data[i]));
			}			
			System.out.println(buffer);
			System.out.println(packet.getLength());
			System.out.println(packet.toString());
			
			String message = new String(packet.getData());
			
			System.out.println("Client " + count++ + "["+ packet.getAddress().getHostAddress() + ":" + packet.getPort() + "]: " + message);			
			
			//lastData = message + " Datagram size: " + Integer.toString(packet.getLength()) + " bytes.";
			//buffer.add(message);
			
			//if (message.trim().equals("ping") && count != 0) {
			//	sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
			//}
		}
	}
	
	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

