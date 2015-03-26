package me.Alex.TSChat.Client.GUI;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class TSChatGUI {
    
    private JFrame frame;
    private JTextField textField;
    private JTextArea textArea;
    private JButton btnSendButton;
    
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
		    TSChatGUI window = new TSChatGUI();
		    window.frame.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }
    
    /**
     * Create the application.
     */
    public TSChatGUI() {
	initialize();
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
	
	this.btnSendButton = new JButton("Senden");
	this.btnSendButton.setSize(100, buttonHeight);
	this.btnSendButton.setLocation((this.frame.getWidth() - (this.btnSendButton.getWidth() + margin)), (this.frame.getHeight() - (this.btnSendButton.getHeight() + margin)));
	this.frame.getContentPane().add(this.btnSendButton);
	
	this.textField = new JTextField();
	this.textField.setSize(this.frame.getWidth() - (this.btnSendButton.getWidth() + 3 * margin), buttonHeight);
	this.textField.setLocation(margin, (this.frame.getHeight() - (this.textField.getHeight() + margin)));
	this.frame.getContentPane().add(this.textField);
	
	/*	JList<String> list = new JList<String>();
		list.setBounds(480, 89, 70, 146);
		this.frame.getContentPane().add(list);
		*/
	
	this.textArea = new JTextArea();
	this.textArea.setEditable(false);
	this.textArea.setSize(this.frame.getWidth() - 2 * margin, this.frame.getHeight() - (buttonHeight + 3 * margin));
	this.textArea.setLocation(margin, margin);
	this.frame.getContentPane().add(this.textArea);
	
	System.out.println(this.btnSendButton.getLocation());
	System.out.println(this.frame.getBounds());
    }
    
    private void resize() {
	
	this.btnSendButton.setLocation((this.frame.getWidth() - (this.btnSendButton.getWidth() + margin)), (this.frame.getHeight() - (this.btnSendButton.getHeight() + margin)));
    }
}
