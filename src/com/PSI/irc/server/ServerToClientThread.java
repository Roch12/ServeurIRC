package com.PSI.irc.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import com.PSI.irc.IfClientServerProtocol;

public class ServerToClientThread extends Thread{
	private User user;
	public Socket socket = null;
	private DefaultListModel<String> clientListModel = null;
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;
	
	public ServerToClientThread(User user, Socket socket,DefaultListModel<String> clientListModel) {
		super();
		this.user=user;
		this.socket = socket;
		this.clientListModel = clientListModel;
	}
	
	List<String> msgToPost=new ArrayList<String>();
	
	public synchronized void post(String msg){
		msgToPost.add(msg);
	}
	
	private synchronized void doPost(){
		try {
			for (String msg : msgToPost) {
					streamOut.writeUTF(msg);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			msgToPost.clear();
		}
	}
	
	public void open() throws IOException {
		streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		streamOut = new DataOutputStream(socket.getOutputStream());
	}
	public void close() throws IOException {
		if (socket != null)
			socket.close();
		if (streamIn != null)
			streamIn.close();
		if (streamOut != null)
			streamOut.close();
	}

	@Override
	public void run() {
		try {
			open();
			boolean done = false;
			while (!done) {
				try {
					if(streamIn.available()>0){
						String line = streamIn.readUTF();
						String[] userMsg=line.split(IfClientServerProtocol.SEPARATOR);
						String login=userMsg[1];
						String msg=userMsg[2];
						done = msg.equals(".bye");
						if(!done){
							
							if(login.equals(user)){
								System.err.println("ServerToClientThread::run(), login!=user"+login);
							}
							if(line.equals(IfClientServerProtocol.DEL + user.getLogin())) {
								BroadcastThread.sendMessage(user,"",IfClientServerProtocol.DEL);
								BroadcastThread.removeClient(user);
								clientListModel.clear();
								for (User user : BroadcastThread.clientTreadsMap.keySet()) {
									clientListModel.addElement(user.getLogin());
								}
								System.out.println("Delete " + user.getLogin());
							}
							else if(line.startsWith(IfClientServerProtocol.Whispers)){
								BroadcastThread.sendPrivateMessage(user, userMsg[3], userMsg[4]);
							}
							else BroadcastThread.sendMessage(user,msg,IfClientServerProtocol.Message);
						}
					}
					else{
						doPost();
					}
				} 
				catch (IOException ioe) {
					done = true;
				}
			}
			close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
