package me.Alex.TSChat;

import me.Alex.TSChat.Client.Client;
import me.Alex.TSChat.Server.Server;


public class Main {
    
    public static void main(String[] args) {
	
	System.out.println("start");
	
	Server server = new Server(8935, "localhost");
	
	Client client = new Client("localhost", 8935);
	
    }
}
