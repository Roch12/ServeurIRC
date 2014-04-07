package com.PSI.irc.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

import com.PSI.controller.ServeurLauncher;
import com.PSI.irc.IfClientServerProtocol;

public class ClientConnectThread extends Thread implements IfClientServerProtocol {
	StyledDocument model=null;
	DefaultStyledDocument modelChat = null;
	
	private boolean canStop=false;
	private ServerSocket server = null;
	
	/**
	 * Affichage des informations de connexion
	 * @param msg String
	 */
	private void printMsg(String msg){
		try {
			if(model!=null){
				model.insertString(model.getLength(), msg+"\n", null);
			}
			System.out.println("PrintMsg : " + msg);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Création du thread de connexion serveur
	 * @param port int
	 * @param model StyledDocument
	 * @param modelChat DefaultStyledDocument
	 */
	public ClientConnectThread(int port, StyledDocument model, DefaultStyledDocument modelChat) {
		try {
			this.model=model;
			this.modelChat = modelChat;
			printMsg("Binding to port " + port + ", please wait  ...");
			server = new ServerSocket(port);
			printMsg("Server started: " + server);
		} 
		catch (IOException ioe) {
			System.out.println(ioe);
		}
	}
	
	@Override
	public void run() {
		while(!canStop){
			printMsg("Waiting for a client ...");
			Socket socket;
			try {
				socket = server.accept();
				printMsg("Client accepted: " + socket);
				
				// Accept new client or close the socket
				acceptClient(socket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Accepter un client sur le serveur
	 * @param socket Socket
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void acceptClient(Socket socket) throws IOException, InterruptedException {
		// Read user login and pwd
		DataInputStream dis=new DataInputStream(socket.getInputStream());
		DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
		dos.writeUTF(LOGIN_PWD);
		while(dis.available()<=0){
			Thread.sleep(100);
		}
		String reponse=dis.readUTF();
		String[] userPwd=reponse.split(SEPARATOR);
		String login=userPwd[1];
		String pwd=userPwd[2];
		User newUser=new User(login, pwd);
		boolean isUserOK=authentication(newUser);
		if(isUserOK){
			
			ServerToClientThread client=new ServerToClientThread(newUser, socket, modelChat);
			dos.writeUTF(OK);

			// Add user
			if(BroadcastThread.addClient(newUser, client)){
				client.start();
				BroadcastThread.loadUserByMessage("Salon principal");
				BroadcastThread.loadAllSalons(client);
				dos.writeUTF(ADD+login+"#Salon principal");
			}
			else
			{
				System.out.println("socket.close()");
				dos.writeUTF(KO);
				dos.close();
				socket.close();
			}
		}
		else{
			System.out.println("socket.close()");
			dos.writeUTF(KO);
			dos.close();
			socket.close();
		}
	}
	
	/**
	 * Authentification de l'utilisateur
	 * @param newUser
	 * @return boolean
	 */
	private boolean authentication(User newUser){
		return BroadcastThread.accept(newUser);
	}

	
	/**
	 * Ouverture du flux
	 * @throws IOException
	 */
	public void open() throws IOException {
	}
	
	/**
	 * Fermeture du flux
	 * @throws IOException
	 */
	public void close() throws IOException {
		System.err.println("server:close()");
		if (server != null)
			server.close();
	}
}
