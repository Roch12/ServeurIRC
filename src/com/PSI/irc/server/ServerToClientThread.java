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
						System.out.println("line : " + line);
						String login ="";
						String msg="";
						String salon ="";
						String newLine = line.substring(IfClientServerProtocol.ADD.length());
						String[] userMsg=newLine.split(IfClientServerProtocol.SEPARATOR);
						System.out.println("UserMSG[1] = " +userMsg[1]);
						login=userMsg[0];
						salon = userMsg[1];
						if(line.startsWith(IfClientServerProtocol.Salon) || line.startsWith(IfClientServerProtocol.Whispers))
						msg =userMsg[2];
						
						done = msg.equals(".bye");
						if(!done){
							
							if(login.equals(user)){
								System.err.println("ServerToClientThread::run(), login!=user"+login);
							}
							if(line.startsWith(IfClientServerProtocol.DEL + user.getLogin())) {
								BroadcastThread.sendMessage(user,salon,"",IfClientServerProtocol.DEL);
								BroadcastThread.removeClient(user);
								clientListModel.clear();
								for (User user : BroadcastThread.clientTreadsMap.keySet()) {
									clientListModel.addElement(user.getLogin());
								}
								System.out.println("Delete " + user.getLogin());
							}
							else if(line.startsWith(IfClientServerProtocol.Whispers)){
								BroadcastThread.sendPrivateMessage(user, salon, msg);
							}
							else if(line.startsWith(IfClientServerProtocol.Salon)){
								BroadcastThread.sendMessage(user,salon,msg,IfClientServerProtocol.Message);
							}
							else if(line.startsWith(IfClientServerProtocol.RemoveFromSalon)){
								BroadcastThread.sendMessage(user,salon,"",IfClientServerProtocol.DEL);
							}
							else if(line.startsWith(IfClientServerProtocol.AddToSalon)){
								BroadcastThread.SalonMap.remove(user);
								System.out.println("ADDtoSalon salon : " + userMsg[2]);
								BroadcastThread.SalonMap.put(user, userMsg[2]);
								BroadcastThread.loadUserByMessage(userMsg[2]);
								BroadcastThread.sendMessage(user,userMsg[2],"",IfClientServerProtocol.ADD);
							}
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
