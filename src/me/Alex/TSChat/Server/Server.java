package me.Alex.TSChat.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import me.Alex.TSChat.Server.Commands.CommandInterpreter;
import me.Alex.TSChat.Server.Commands.IChatCommand;


public class Server {
    
    private static ServerSocket server;
    
    private static ArrayList<ClientConnection> connections = new ArrayList<ClientConnection>();
    
    private static String authString;
    
    public static void main(String[] args) {
	new Server(8935, "localhost");
    }
    
    public Server(int port, String host) {
	
	try {
	    server = new ServerSocket(port);
	    new CommandInterpreter();
	    authString = UUID.randomUUID().toString().substring(0, UUID.randomUUID().toString().length() / 2);
	    
	    start();
	    System.out.println("Server gestartet! Admin pass: " + authString);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    private void start() {
	
	Thread t = new Thread(new Runnable() {
	    
	    @Override
	    public void run() {
		try {
		    ClientConnection ch = new ClientConnection(server.accept());
		    System.out.println(ch.getSocket().toString() + " connected!");
		    connections.add(ch);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		this.run();
	    }
	});
	
	t.start();
    }
    
    public static void stop() throws IOException {
	
	for (ClientConnection connection : connections) {
	    connection.stop();
	}
	connections.clear();
	server.close();
    }
    
    private static ClientConnection getConnection(String nick) {
	for (ClientConnection connection : connections) {
	    if (connection.getNickName().equals(nick)) {
		return connection;
	    }
	}
	return null;
    }
    
    public static boolean setNick(String nickName, String newNick) {
	ClientConnection conn = getConnection(nickName);
	if (conn != null) {
	    conn.setNickName(newNick);
	    return true;
	}
	return false;
    }
    
    private void sendMessage(String message, ClientConnection sender) {
	
	for (ClientConnection connection : connections) {
	    if (!connection.equals(sender)) {
		System.out.println(message + " SendTo " + connection.getNickName());
		String nick = sender.isAuthenticated() ? sender.getNickName() + "[Admin]" : sender.getNickName();
		connection.sendToClient(nick + ": " + message);
	    }
	}
    }
    
    public static void sendMessageToNick(String nickName, String message) {
	ClientConnection conn = getConnection(nickName);
	if (conn != null) {
	    conn.sendToClient(message);
	}
    }
    
    public static void broadcast(String message) {
	for (ClientConnection connection : connections) {
	    connection.sendToClient("[Server] " + message);
	}
    }
    
    public static boolean authenticate(String nickName, String pass) {
	
	if (pass.equals(authString)) {
	    ClientConnection conn = getConnection(nickName);
	    if (conn != null) {
		conn.setAuthenticated(true);
		return true;
	    }
	    
	}
	return false;
    }
    
    public static void disconnectAll() {
	System.out.println(connections.size());
	for (ClientConnection connection : connections) {
	    System.out.println("Will disconnect " + connection.getNickName());
	    connection.stop();
	}
	connections.clear();
    }
    
    public static void disconnect(String nickName) {
	
	ClientConnection conn = getConnection(nickName);
	if (conn != null) {
	    System.out.println("Will disconnect " + nickName);
	    conn.stop();
	    connections.remove(conn);
	}
	
    }
    
    public static boolean isAuthenticated(String nickName) {
	ClientConnection conn = getConnection(nickName);
	if (conn != null) {
	    return conn.isAuthenticated();
	}
	return false;
    }
    
    private class ClientConnection implements Runnable {
	
	private Socket client;
	
	private String nickName;
	
	private BufferedReader reader;
	private PrintWriter writer;
	
	private boolean running;
	private boolean authenticated;
	
	public ClientConnection(Socket client) {
	    this.client = client;
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
			    IChatCommand command = CommandInterpreter.execute(getNickName(), message);
			    if (command == null) {
				sendMessage(message, this);
			    } else {
				// System.out.println(command.getCommand());
			    }
			}
		    } else {
			// Connection reset by partner
			disconnect(getNickName());
		    }
		}
	    } catch (Exception e) {
		e.printStackTrace();
		disconnect(getNickName());
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
		System.out.println(getSocket() + " stoped!");
		getSocket().close();
		sendMessage(getNickName() + " has disconnected!", this);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }
}
