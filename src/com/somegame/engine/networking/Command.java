package com.somegame.engine.networking;

public class Command {

	protected boolean isReliable;
	protected byte id;
	
	public Command(boolean isReliable_, int id_) {
		isReliable = isReliable_;
		id = (byte) id_;
	}
	
	public byte[] toBytes() {
		return null;
	}
		
}
