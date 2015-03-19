package me.Alex.TSChat.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.Alex.TSChat.Server.Commands.CommandInterpreter;
import me.Alex.TSChat.Server.Commands.IChatCommand;


public class ClientConnection implements Runnable {
    
    private Socket client;
    private Server server;
    
    private String nickName;
    
    private BufferedReader reader;
    private PrintWriter writer;
    
    private boolean running;
    private boolean authenticated;
    
    public ClientConnection(Socket client, Server server) {
	this.client = client;
	this.server = server;
	
	this.nickName = client.getInetAddress().getHostAddress();
	this.authenticated = false;
	
	try {
	    this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
	    this.writer = new PrintWriter(client.getOutputStream());
	} catch (IOException e) {
	    e.printStackTrace();
	}
	
	this.running = true;
	new Thread(this).start();
    }
    
    @Override
    public void run() {
	try {
	    
	    while (this.running) {
		
		String message = this.reader.readLine();
		if (message != null) {
		    System.out.println("Empfangen[" + new SimpleDateFormat("dd.MM-HH:mm:ss").format((new Date())) + "]: " + message + " von " + getSocket().getInetAddress().toString() + " (" + this.nickName + ")");
		    
		    if (message.startsWith("_u")) {
			
			int id = Integer.parseInt(message.replace("_u", "").substring(0, 4));
			
			switch (id) {
			    case 1236:
				this.nickName = message.split(Integer.toString(id))[1];
				break;
			    
			    default:
				break;
			}
			
		    } else {
			IChatCommand command = CommandInterpreter.execute(this, message);
			if (command == null) {
			    Server.sendMessage(message, this);
			} else {
			    // System.out.println(command.getCommand());
			}
		    }
		} else {
		    // Connection reset by partner
		    Server.disconnect(this);
		}
	    }
	} catch (SocketException e) {
	    System.err.println(e.getMessage());
	} catch (IOException e) {
	    e.printStackTrace();
	    Server.disconnect(this);
	}
    }
    
    public void sendToClient(String message) {
	this.writer.println(message);
	this.writer.flush();
    }
    
    public void setAuthenticated(boolean auth) {
	this.authenticated = auth;
    }
    
    public void setNickName(String nickName) {
	this.nickName = nickName;
    }
    
    public Socket getSocket() {
	return this.client;
    }
    
    public Server getServer() {
	return this.server;
    }
    
    public String getNickName() {
	return this.nickName;
    }
    
    public boolean isAuthenticated() {
	return this.authenticated;
    }
    
    public void stop() {
	try {
	    sendToClient("_u1200");
	    this.running = false;
	    
	    this.writer.close();
	    this.reader.close();
	    getSocket().close();
	    System.out.println(getSocket() + " stoped!");
	    Server.broadcast(getNickName() + " has disconnected!");
	} catch (SocketException e) {
	    System.err.println(e.getMessage());
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
