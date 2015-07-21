package org.antislashn.chat.start;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

	/***** ATTRIBUTES *****/
	
	private int port = 45123;
	private ServerSocket serverSocket;
	private List<Socket> clientsConnectes = new ArrayList<Socket>();
	
	
	/***** CONSTRUCTORS *****/

	public ChatServer() {
	}
	
	public ChatServer(int port) {
		this();
		this.port = port;
	}
	
	
	/***** METHODS *****/

	public void start() {
		Socket clientSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println(">>> serveur démarré sur le port " + port);
			while (true) {
				clientSocket = serverSocket.accept();
				clientsConnectes.add(clientSocket);
				GestionClient gestionClient = new GestionClient(clientSocket);
				gestionClient.start();
			}
		} catch (IOException e) {
			System.out.println(">>> ERREUR DEMARRAGE SERVEUR");
			e.printStackTrace();
		}
	}

	private class GestionClient extends Thread{
		private Socket clientSocket;
		private Writer out;
		private BufferedReader in;
		
		public GestionClient(Socket clientSocket)  {
			this.clientSocket = clientSocket;
			try {
				this.out = new OutputStreamWriter(clientSocket.getOutputStream());
				this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			System.out.println(">>> Client accepté : "+clientSocket.toString());
			while(true){
				try {
					String line = in.readLine();
//					System.out.println("=> Message reçu : "+line);
					for(Socket socket : ChatServer.this.clientsConnectes){
						Writer out = new OutputStreamWriter(socket.getOutputStream());
						out.write(line+"\n");
						out.flush();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/***** MAIN *****/
	
	public static void main(String[] args) {
		ChatServer serverChat = new ChatServer();
		serverChat.start();
	}
}
