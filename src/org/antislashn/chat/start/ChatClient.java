package org.antislashn.chat.start;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ChatClient extends JFrame implements ActionListener, Runnable {

	/***** ATTRIBUTES *****/
	
	private String user;
	private String host;
	private int port=45123;
	private Socket socket;
	private Writer out;
	private BufferedReader in;
	
	private JTextArea listeMessages = new JTextArea(10, 32);
	private JTextField saisie = new JTextField(32);
	
	
	/***** CONSTRUCTORS *****/

	public ChatClient(String name, String hostName) throws UnknownHostException, IOException {
		this.user = name;
		this.host = hostName;
		
		listeMessages.setEditable(false);
		listeMessages.setBackground(Color.LIGHT_GRAY);
		saisie.addActionListener(this);

		Container content = getContentPane();
		content.add(new JScrollPane(listeMessages), BorderLayout.CENTER);
		content.add(saisie, BorderLayout.SOUTH);

		setTitle("Chat Client : [" + user + "]");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		saisie.requestFocusInWindow();
		setVisible(true);
		
		// Connexion au serveur
		this.socket = new Socket(host, port);
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new OutputStreamWriter(socket.getOutputStream());
		new Thread(this).start();
	}
	
	
	/***** METHODS *****/

	public void actionPerformed(ActionEvent e) {
		String message = saisie.getText();
		try {
			out.write(message+"\n");
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		saisie.setText("");
		saisie.requestFocusInWindow();
	}

	@Override
	public void run() {
		while(true){
			try {
				String line = in.readLine();
				listeMessages.setText(listeMessages.getText()+"\n"+line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/***** MAIN *****/
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws UnknownHostException, IOException {
		String name = JOptionPane.showInputDialog(null,"Quel est votre nom ?","");
		String host = "localhost";
		ChatClient client = new ChatClient(name, host);
	}
}
