package me.Alex.TSChat.Server.Commands;

import me.Alex.TSChat.Server.ClientConnection;


public interface IChatCommand {
    
    public String getCommand();
    
    public String getPermission();
    
    public boolean execute(ClientConnection executor, String... args);
}
