package me.Alex.TSChat.Client;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import me.Alex.TSChat.Client.GUI.TSChatGUI;


public class Client implements Runnable {
    
    private String host;
    private int port;
    
    private Socket server;
    
    private BufferedReader reader;
    private PrintWriter writer;
    private Thread serverReader;
    
    private TSChatGUI gui;
    
    private boolean running;
    
    public static void main(String[] args) {
	System.out.print("Host: ");
	Scanner scann = new Scanner(System.in);
	String host = scann.next();
	scann.close();
	new Client(host, 8935);
    }
    
    public Client(String ip, int port) {
	EventQueue.invokeLater(new Runnable() {
	    
	    @Override
	    public void run() {
		try {
		    Client.this.gui = new TSChatGUI(Client.this);
		    getGUI().addMessage("Du bist verbunden und kannst nun schreiben!");
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
	
	System.out.println("Verbinde zu Server " + ip + ":" + port + " ...");
	
	try {
	    this.server = new Socket(ip, port);
	    
	    this.reader = new BufferedReader(new InputStreamReader(this.server.getInputStream()));
	    this.writer = new PrintWriter(this.server.getOutputStream());
	    
	    this.running = true;
	    
	    this.serverReader = new Thread(this);
	    this.serverReader.start();
	    
	} catch (IOException e) {
	    e.printStackTrace();
	}
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
	    if (getGUI() != null && getGUI().isActive()) {
		getGUI().closeGUI();
	    }
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
    
    public void sendNick(String nickname) {
	sendMessage("_u1236" + nickname);
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
			System.out.println("Empfangen: [" + new SimpleDateFormat("dd.MM-HH:mm:ss").format((new Date())) + "] " + message);
		    }
		} else {
		    // Connection reset by partner
		    System.out.println("Disconnected from Server!");
		    stop();
		}
	    }
	} catch (SocketException e) {
	    System.err.println(e.getMessage());
	    stop();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
}
