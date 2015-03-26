package me.Alex.TSChat.Client.GUI;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JTextPane;


public class TSChatGUI {
    
    private JFrame frame;
    private JTextField textField;
    
    private JButton btnSendButton;
    
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
	this.frame.setMinimumSize(new Dimension(600, 400));
	this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.frame.getContentPane().setLayout(null);
	
	this.btnSendButton = new JButton("Senden");
	this.btnSendButton.setSize(100, 50);
	this.btnSendButton.setLocation((this.frame.getHeight() - (this.btnSendButton.getHeight() + 0)), this.frame.getWidth() - (this.btnSendButton.getWidth() + 10));
	this.frame.getContentPane().add(this.btnSendButton);
	
	this.textField = new JTextField();
	this.textField.setBounds(24, 206, 290, 37);
	this.frame.getContentPane().add(this.textField);
	this.textField.setColumns(10);
	
	JTextPane textPane = new JTextPane();
	textPane.setBounds(31, 31, 283, 165);
	this.frame.getContentPane().add(textPane);
	
	JList<String> list = new JList<String>();
	list.setBounds(347, 31, 70, 146);
	this.frame.getContentPane().add(list);
	System.out.println(this.btnSendButton.getLocation());
	System.out.println(this.frame.getBounds());
    }
    
    private void resize() {
	
	this.btnSendButton.setBounds(this.frame.getHeight() - (this.btnSendButton.getHeight() + 20), this.frame.getWidth() - (this.btnSendButton.getWidth() + 20), 100, 50);
	
    }
}
