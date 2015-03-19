package me.Alex.TSChat.Server.Commands;

public interface IChatCommand {

    public String getCommand();

    public String getPermission();

    public boolean execute(String executorNick, String... args);
}
