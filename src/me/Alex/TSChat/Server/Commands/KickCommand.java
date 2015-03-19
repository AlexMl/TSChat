package me.Alex.TSChat.Server.Commands;

import me.Alex.TSChat.Server.Server;


public class KickCommand implements IChatCommand {
    
    private String name;
    private String permission;
    
    public KickCommand() {
	this.name = "kick";
	this.permission = "admin.kick";
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
	    String nick = args[0];
	    Server.disconnect(nick);
	    Server.sendMessage(executorNick, "Du hast " + nick + " gekickt!");
	    return true;
	}
	return false;
    }
    
}