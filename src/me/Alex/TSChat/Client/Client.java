package me.Alex.TSChat.Client;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import me.Alex.TSChat.Client.GUI.TSChatGUI;


public class Client implements Runnable {
    
    private String nickName;
    
    private String host;
    private int port;
    
    private Socket server;
    
    private BufferedReader reader;
    private PrintWriter writer;
    private Thread thread;
    
    private TSChatGUI gui;
    
    private boolean running;
    
    private static Scanner scann;
    
    public static void main(String[] args) {
	System.out.print("Host: ");
	scann = new Scanner(System.in);
	String host = scann.next();
	
	new Client(host, 8935);
    }
    
    public Client(String ip, int port) {
	
	EventQueue.invokeLater(new Runnable() {
	    
	    @Override
	    public void run() {
		try {
		    Client.this.gui = new TSChatGUI(Client.this);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
	
	System.out.print("\n");
	System.out.print("Dein Nickname: ");
	this.nickName = scann.next();
	this.running = true;
	System.out.println("Hallo " + this.nickName + "! verbinde zu Server " + ip + ":" + port + "...");
	
	try {
	    this.server = new Socket(ip, port);
	    
	    this.reader = new BufferedReader(new InputStreamReader(this.server.getInputStream()));
	    this.writer = new PrintWriter(this.server.getOutputStream());
	    
	    this.thread = new Thread(this);
	    this.thread.start();
	    sendMessage("_u1236" + this.nickName);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	getGUI().addMessage("Du bist verbunden und kannst nun schreiben!");
	System.out.println("Du bist verbunden und kannst nun schreiben!");
	System.out.print("Eingabe: ");
	
	while (scann.hasNext() && this.running) {
	    String message = scann.nextLine();
	    if (!message.isEmpty()) {
		sendMessage(message);
	    }
	}
	scann.close();
    }
    
    public String getIP() {
	return this.host;
    }
    
    public int getPort() {
	return this.port;
    }
    
    public TSChatGUI getGUI() {
	return this.gui;
    }
    
    public void stop() {
	try {
	    this.running = false;
	    
	    this.writer.close();
	    this.reader.close();
	    this.server.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    public void sendMessage(String message) { // From Client to Server
	this.writer.println(message);
	this.writer.flush();
	if (!message.startsWith("_u12")) {
	    System.out.println("Gesendet: [" + new SimpleDateFormat("dd.MM-HH:mm:ss").format((new Date())) + "] " + message);
	}
    }
    
    @Override
    public void run() {	// From Server to Client
    
	try {
	    while (this.running) {
		String message = this.reader.readLine();
		if (message != null) {
		    
		    if (message.startsWith("_u12")) {
			
			int id = Integer.parseInt(message.replace("_u", "").substring(0, 4));
			
			switch (id) {
			    case 1200:
				this.stop();
				return;
				
			    default:
				break;
			}
			
		    } else {
			getGUI().addMessage(message);
			System.out.println(message);
		    }
		} else {
		    // Connection reset by partner
		    System.out.println("Disconnected from Server!");
		    stop();
		}
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
}
