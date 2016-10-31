package com.somegame.engine.networking;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.somegame.engine.networking.commands.LoginCommand;

public class ServerInterface extends Thread {

	public static final int MAX_CLIENTS = 10;
	public static final int MAX_RELIABLE_BUFFER = 100;
	public static final int MAX_OUTPUT_COMMANDS = 1000;
	public static final int MAX_PACKET_SIZE = 1400;

	private DatagramSocket socket;
	// SEPARAR A PARTE DE CLIENTS PARA OBTER UMA INTERFACE UDP GENERICA
	private ArrayList<ClientInformation> clients;

	private ArrayList<Command> receivedCommands;

	private LinkedList<Command> reliableBuffer;

	private int sequence;
	private int lastReliableAck;

	private String password;

	public ServerInterface() {
		password = "batata";
		clients = new ArrayList<ClientInformation>();

		receivedCommands = new ArrayList<Command>();

		outputCommands = new LinkedList<Command>();
		reliableBuffer = new LinkedList<Command>();

		sequence = 0;
		lastReliableAck = 0;

		try {
			socket = new DatagramSocket(6789);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	private byte[] translateCommands(ArrayList<Command> c) {
		return null;
	}

	private ArrayList<Command> translateData(byte[] b) {
		ArrayList<Command> result = new ArrayList<Command>();

		String rawMessage = "";

		try {
			rawMessage = new String(b, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			String[] tokens = rawMessage.split("%");

			int messageSequence = Integer.parseInt(tokens[0]);
			int lastAck = Integer.parseInt(tokens[1]);
			boolean reliableBit = ((Integer.parseInt(tokens[2]) & 0x1) == 1) ? true : false;

			for (int i = 3; i < tokens.length; i++) {
				String[] subTokens = tokens[i].split("\\$");
				int commandId = Integer.parseInt(subTokens[0]);

				switch (commandId) {
				case 1:
					System.out.println("pacote 1 recebido");
					break;
				default:
				}
			}
		} catch (Exception e) {
		}

		return result;
	}

	private void login(byte[] data, InetAddress address, int port) {
		String rawMessage = "";

		try {
			rawMessage = new String(data, "UTF-8").trim();

			String[] tokens = rawMessage.split("%");

			for (int i = 3; i < tokens.length; i++) {
				String[] subTokens = tokens[i].split("\\$");
				int commandId = Integer.parseInt(subTokens[0]);

				if (commandId != 0) {
					throw new Exception();
				} else {
					LoginCommand login = new LoginCommand(subTokens[1], subTokens[2]);
					
					if (login.getPassword().equals(password)) {
						System.out.println("Realizando login: " + login.getName() + " " + address.getHostAddress() + ":" + port);
						clients.add(new ClientInformation(address, port, login.getName()));
					} else {
						System.out.println("senha errada!");
						throw new Exception();
					}
				}
			}
		} catch (Exception e) {
		}

	}

	public void inputCommand(Command c) {

	}

	public ArrayList<Command> getCommands() {
		return receivedCommands;
	}

	public void flush() {

	}

	public void run() {
		while (true) {
			byte[] data = new byte[MAX_PACKET_SIZE];
			DatagramPacket packet = new DatagramPacket(data, data.length);

			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}

			boolean clientDetected = false;
			for (ClientInformation i : clients) {
				if (packet.getAddress().equals(i.getIp()) && packet.getPort() == i.getPort()) {
					clientDetected = true;
				}
			}

			if (clientDetected) {
				receivedCommands = translateData(packet.getData());
			} else if (clients.size() < MAX_CLIENTS && !clientDetected) {
				login(packet.getData(), packet.getAddress(), packet.getPort());
			}
		}
	}

}
