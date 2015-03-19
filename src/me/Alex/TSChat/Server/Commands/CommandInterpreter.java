package me.Alex.TSChat.Server.Commands;

import java.util.ArrayList;
import java.util.Arrays;

import me.Alex.TSChat.Server.Server;


public class CommandInterpreter {
    
    private static ArrayList<IChatCommand> commands = new ArrayList<IChatCommand>();
    
    public CommandInterpreter() {
	
	commands.add(new KickCommand());
	commands.add(new KickallCommand());
	commands.add(new NickCommand());
	commands.add(new LoginCommand());
    }
    
    public static IChatCommand execute(String executorNick, String message) {
	
	if (message.startsWith("/")) {
	    
	    message = message.replace("/", "");
	    String commandString;
	    String[] args;
	    
	    if (message.contains(" ")) {
		args = message.split(" ");
		commandString = args[0];
		args = Arrays.copyOfRange(args, 1, args.length);
	    } else {
		args = new String[] {};
		commandString = message;
	    }
	    
	    for (IChatCommand command : commands) {
		System.out.println(command.getCommand() + ":" + commandString);
		if (command.getCommand().equalsIgnoreCase(commandString)) {
		    if (command.getPermission().contains("admin")) {
			if (Server.isAuthenticated(executorNick)) {
			    command.execute(executorNick, args);
			    return command;
			} else {
			    Server.sendMessageToNick(executorNick, "Du hast nicht die nötigen Rechte!");
			    return command;
			}
		    } else {
			command.execute(executorNick, args);
			return command;
		    }
		}
	    }
	}
	return null;
    }
}
