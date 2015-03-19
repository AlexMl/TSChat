package me.Alex.TSChat.Server.Commands;

import me.Alex.TSChat.Server.ClientConnection;


public class HelpCommand implements IChatCommand {
    
    private String command;
    private String permission;
    
    public HelpCommand() {
	this.command = "help";
	this.permission = "";
    }
    
    @Override
    public String getCommand() {
	return this.command;
    }
    
    @Override
    public String getPermission() {
	return this.permission;
    }
    
    @Override
    public boolean execute(ClientConnection executor, String... args) {
	// TODO Auto-generated method stub
	return false;
    }
    
}
