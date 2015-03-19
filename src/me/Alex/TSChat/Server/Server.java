package me.Alex.TSChat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.UUID;

import me.Alex.TSChat.Server.Commands.CommandInterpreter;


public class Server {
    
    private static ServerSocket server;
    
    private static Server instance;
    
    private static ArrayList<ClientConnection> connections = new ArrayList<ClientConnection>();
    
    private static String authString;
    
    public static void main(String[] args) {
	new Server(8935, "localhost");
    }
    
    public Server(int port, String host) {
	
	try {
	    instance = this;
	    server = new ServerSocket(port);
	    new CommandInterpreter();
	    authString = UUID.randomUUID().toString().substring(4, UUID.randomUUID().toString().length() / 4 + 4);
	    
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
		    ClientConnection ch = new ClientConnection(server.accept(), instance);
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
	    disconnect(connection);
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
    
    public static ClientConnection[] getConnections() {
	
	ClientConnection[] connArray = new ClientConnection[connections.size()];
	
	for (ClientConnection connection : connections) {
	    connArray[connections.indexOf(connection)] = connection;
	}
	return connArray;
    }
    
    public static void setNick(ClientConnection conn, String nickName) {
	conn.setNickName(nickName);
    }
    
    public static void sendMessage(String message, ClientConnection sender) {
	
	for (ClientConnection connection : connections) {
	    if (!connection.equals(sender)) {
		System.out.println(message + " SendTo " + connection.getNickName());
		String nick = sender.isAuthenticated() ? sender.getNickName() + "[Admin]" : sender.getNickName();
		connection.sendToClient(nick + ": " + message);
	    }
	}
    }
    
    public static void sendMessageToClient(ClientConnection conn, String message) {
	conn.sendToClient(message);
    }
    
    public static void broadcast(String message) {
	for (ClientConnection connection : connections) {
	    connection.sendToClient("[Server] " + message);
	}
    }
    
    public static boolean authenticate(ClientConnection conn, String pass) {
	
	if (pass.equals(authString)) {
	    conn.setAuthenticated(true);
	    return true;
	}
	return false;
    }
    
    public static void disconnect(String nickName) {
	
	ClientConnection conn = getConnection(nickName);
	if (conn != null) {
	    disconnect(conn);
	}
    }
    
    public static void disconnect(ClientConnection connection) {
	System.out.println("Will disconnect " + connection.getNickName());
	connection.stop();
	connections.remove(connection);
    }
    
    public static boolean isAuthenticated(ClientConnection conn) {
	return conn.isAuthenticated();
    }
}
