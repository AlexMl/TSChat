package me.Alex.TSChat.Server.Commands;

import me.Alex.TSChat.Server.ClientConnection;
import me.Alex.TSChat.Server.Server;


public class LoginCommand implements IChatCommand {
    
    private String name;
    private String permission;
    
    public LoginCommand() {
	this.name = "login";
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
    public boolean execute(ClientConnection executor, String... args) {
	
	if (args.length == 1) {
	    String pass = args[0];
	    boolean success = Server.authenticate(executor, pass);
	    
	    if (success) {
		Server.sendMessageToClient(executor, "Du bist jetzt als Admin angemeldet!");
	    } else {
		Server.sendMessageToClient(executor, "Die angegebene Passphrase ist falsch!");
	    }
	    
	    return true;
	}
	return false;
    }
}
