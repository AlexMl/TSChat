package me.Alex.TSChat.Server.Commands;

import me.Alex.TSChat.Server.Server;


public class NickCommand implements IChatCommand {
    
    private String name;
    private String permission;
    
    public NickCommand() {
	this.name = "nick";
	this.permission = "";
    }
    
    @Override
    public String getCommand() {
	return this.name;
    }
    
    @Override
    public String getPermission() {
	return this.permission;
    }
    
    @Override
    public boolean execute(String executorNick, String... args) {
	if (args.length == 1) {
	    
	    String newNick = args[0];
	    boolean success = Server.setNick(executorNick, newNick);
	    if (success) {
		Server.sendMessageToNick(newNick, "Nick changed to " + newNick);
	    } else {
		Server.sendMessageToNick(executorNick, "Nickchange error!");
	    }
	    return true;
	}
	return false;
    }
    
}
