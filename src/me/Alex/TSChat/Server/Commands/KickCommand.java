package me.Alex.TSChat.Server.Commands;

import me.Alex.TSChat.Server.ClientConnection;
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
    public boolean execute(ClientConnection executor, String... args) {
	if (args.length == 1) {
	    String nick = args[0];
	    boolean success = Server.disconnect(nick);
	    if (success) {
		Server.sendMessageToClient(executor, "Du hast " + nick + " gekickt!");
	    } else {
		Server.sendMessageToClient(executor, nick + " ist nicht online!");
	    }
	    
	    return true;
	}
	return false;
    }
    
}
