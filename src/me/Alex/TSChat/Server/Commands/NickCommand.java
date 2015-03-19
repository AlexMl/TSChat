package me.Alex.TSChat.Server.Commands;

import me.Alex.TSChat.Server.Server;


public class NickCommand implements IChatCommand {
    
    private String name;
    private String permission;
    
    public NickCommand() {
	name = "nick";
	permission = "";
    }
    
    @Override
    public String getCommand() {
	return name;
    }
    
    @Override
    public String getPermission() {
	return permission;
    }
    
    @Override
    public boolean execute(String executorNick, String... args) {
	if (args.length == 1) {
	    
	    String newNick = args[0];
	    boolean success = Server.setNick(executorNick, newNick);
	    if (success) {
		Server.sendMessage(newNick, "Nick changed to " + newNick);
	    } else {
		Server.sendMessage(executorNick, "Nickchange error!");
	    }
	    return true;
	}
	return false;
    }
    
}
