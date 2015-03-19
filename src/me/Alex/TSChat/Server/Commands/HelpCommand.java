package me.Alex.TSChat.Server.Commands;

import me.Alex.TSChat.Server.ClientConnection;
import me.Alex.TSChat.Server.Server;


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
	
	StringBuilder builder = new StringBuilder();
	
	builder.append("-------- Available Commands ---------\n");
	
	for (IChatCommand command : CommandInterpreter.getCommands()) {
	    builder.append("/" + command.getCommand() + ", ");
	}
	
	builder.replace(builder.length() - 2, builder.length(), "");
	Server.sendMessageToClient(executor, builder.toString());
	return false;
    }
}
