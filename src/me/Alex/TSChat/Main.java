package me.Alex.TSChat;

import me.Alex.TSChat.Client.GUI.TSChatGUI;


public class Main {
    
    public static void main(String[] args) {
	
	for (int i = 0; i < 10; i++) {
	    new TSChatGUI("" + i);
	}
	
    }
}
