package com.somegame.engine.networking.commands;

import com.somegame.engine.networking.Command;

public class LoginCommand extends Command {

	private String name;
	private String password;
	
	public LoginCommand(String name_, String password_) {
		super(false, 0);
		setName(name_);
		setPassword(password_);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public byte[] toBytes() {
		
		return (name + "$" + password).getBytes();
	}
	
}
