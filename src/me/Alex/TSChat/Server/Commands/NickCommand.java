package me.Alex.TSChat.Server.Commands;

import me.Alex.TSChat.Server.ClientConnection;
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
    public boolean execute(ClientConnection executor, String... args) {
	if (args.length == 1) {
	    
	    String nickName = args[0];
	    String oldNick = executor.getNickName();
	    Server.setNick(executor, nickName);
	    Server.sendMessageToClient(executor, "Nick changed to " + nickName);
	    Server.sendMessage(oldNick + " ist jetz als " + nickName + " bekannt!", executor);
	    return true;
	}
	return false;
    }
    
}
