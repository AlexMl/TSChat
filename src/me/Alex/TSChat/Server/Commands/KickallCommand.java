package me.Alex.TSChat.Server.Commands;

import me.Alex.TSChat.Server.ClientConnection;
import me.Alex.TSChat.Server.Server;


public class KickallCommand implements IChatCommand {
    
    private String name;
    private String permission;
    
    public KickallCommand() {
	this.name = "kickall";
	this.permission = "admin.kickall";
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
	
	for (ClientConnection conn : Server.getConnections()) {
	    if (!conn.equals(executor)) {
		Server.disconnect(conn);
	    }
	}
	
	return true;
    }
}
