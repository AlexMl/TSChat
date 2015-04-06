package me.Alex.TSChat.Client.GUI;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import me.Alex.TSChat.Client.Client;


public class TSChatGUI {
    
    private JFrame frame;
    private JTextField textField;
    private JTextArea textArea;
    private JButton btnSendButton;
    
    private Client client;
    
    private boolean active;
    
    private static final int margin = 15;
    private static final int buttonHeight = 40;
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    
	    @Override
	    public void run() {
		try {
		    new TSChatGUI(null);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }
    
    /**
     * Create the application.
     */
    public TSChatGUI(Client client) {
	this.client = client;
	this.active = true;
	initialize();
	
	String nick = getNewNick();
	if (nick == null) {
	    closeGUI();
	    return;
	}
	client.sendNick(nick);
	
	this.frame.setVisible(true);
    }
    
    /**
     * Get a new nick from user
     */
    public String getNewNick() {
	String nick = JOptionPane.showInputDialog("Gib einen Nickname an:", "");
	
	if (nick != null && nick.isEmpty()) {
	    return getNewNick();
	}
	return nick;
    }
    
    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
	this.frame = new JFrame();
	this.frame.setBounds(100, 100, 600, 500);
	this.frame.setMinimumSize(new Dimension(600, 500));
	this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.frame.getContentPane().setLayout(null);
	this.frame.addComponentListener(new GUIListener());
	this.frame.addWindowListener(new GUIListener());
	
	this.btnSendButton = new JButton("Senden");
	this.btnSendButton.addActionListener(new GUIListener());
	// this.btnSendButton.setSize(100, buttonHeight);
	// this.btnSendButton.setLocation((this.frame.getWidth() - (this.btnSendButton.getWidth() + margin)), (this.frame.getHeight() - (this.btnSendButton.getHeight() + margin)));
	this.frame.getContentPane().add(this.btnSendButton);
	
	this.textField = new JTextField();
	this.textField.addKeyListener(new GUIListener());
	// this.textField.setSize(this.frame.getWidth() - (this.btnSendButton.getWidth() + 3 * margin), buttonHeight);
	// this.textField.setLocation(margin, (this.frame.getHeight() - (this.textField.getHeight() + margin)));
	this.frame.getContentPane().add(this.textField);
	
	/*	JList<String> list = new JList<String>();
		list.setBounds(480, 89, 70, 146);
		this.frame.getContentPane().add(list);
		*/
	
	this.textArea = new JTextArea();
	this.textArea.setEditable(false);
	// this.textArea.setSize(this.frame.getWidth() - 2 * margin, this.frame.getHeight() - (buttonHeight + 3 * margin));
	// this.textArea.setLocation(margin, margin);
	this.frame.getContentPane().add(this.textArea);
	
	resize();
    }
    
    private void resize() {
	this.btnSendButton.setSize(100, buttonHeight);
	this.btnSendButton.setLocation((this.frame.getWidth() - (this.btnSendButton.getWidth() + margin)), (this.frame.getHeight() - (buttonHeight + margin)));
	
	this.textField.setSize(this.frame.getWidth() - (this.btnSendButton.getWidth() + 3 * margin), buttonHeight);
	this.textField.setLocation(margin, (this.frame.getHeight() - (this.textField.getHeight() + margin)));
	
	this.textArea.setSize(this.frame.getWidth() - 2 * margin, this.frame.getHeight() - (buttonHeight + 3 * margin));
	this.textArea.setLocation(margin, margin);
    }
    
    private Client getClient() {
	return this.client;
    }
    
    public void closeGUI() {
	this.active = false;
	this.frame.dispose();
	getClient().stop();
    }
    
    public void sendMessage(String message) {
	getClient().sendMessage(TSChatGUI.this.textField.getText());
	addMessage(TSChatGUI.this.textField.getText());
	TSChatGUI.this.textField.setText("");
    }
    
    public void addMessage(String message) {
	this.textArea.append(message + "\n");
    }
    
    public boolean isActive() {
	return this.active;
    }
    
    private class GUIListener implements ActionListener, ComponentListener, KeyListener, WindowListener {
	
	@Override
	public void actionPerformed(ActionEvent event) {
	    
	    if (event.getSource() instanceof JButton) {
		if (event.getActionCommand().equalsIgnoreCase(TSChatGUI.this.btnSendButton.getText())) {
		    if (!TSChatGUI.this.textField.getText().isEmpty()) {
			sendMessage(TSChatGUI.this.textField.getText());
		    } else {
			JOptionPane.showMessageDialog(null, "Du kannst keine leeren Nachrichten senden!");
		    }
		    
		}
	    }
	    
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
	    resize();
	}
	
	@Override
	public void componentMoved(ComponentEvent e) {
	}
	
	@Override
	public void componentShown(ComponentEvent e) {
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
		if (!TSChatGUI.this.textField.getText().isEmpty()) {
		    sendMessage(TSChatGUI.this.textField.getText());
		} else {
		    JOptionPane.showMessageDialog(null, "Du kannst keine leeren Nachrichten senden!");
		}
	    }
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
	    closeGUI();
	}
	
	@Override
	public void windowClosed(WindowEvent e) {
	}
	
	@Override
	public void windowIconified(WindowEvent e) {
	}
	
	@Override
	public void windowDeiconified(WindowEvent e) {
	}
	
	@Override
	public void windowActivated(WindowEvent e) {
	}
	
	@Override
	public void windowDeactivated(WindowEvent e) {
	}
	
    }
}
