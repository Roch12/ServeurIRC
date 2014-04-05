package com.PSI.irc.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

import com.PSI.controller.ServeurLauncher;
import com.PSI.irc.IfClientServerProtocol;

public class ServerToClientThread extends Thread{
	
    public static final String BOLD_ITALIC = "BoldItalic";
    public static final String GRAY_PLAIN = "Gray";
	private User user;
	public Socket socket = null;
	public long time = System.currentTimeMillis();
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;
	private DefaultStyledDocument doc = null;
	
	public ServerToClientThread(User user, Socket socket, DefaultStyledDocument doc) {
		super();
		this.user=user;
		this.socket = socket;
		this.doc = doc;
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
								System.out.println("Delete " + user.getLogin());
								ServeurLauncher.LoadTree();
							}
							else if(line.startsWith(IfClientServerProtocol.Whispers)){
								BroadcastThread.sendPrivateMessage(user, salon, msg);
							}
							else if(line.startsWith(IfClientServerProtocol.Salon)){
								
								BroadcastThread.sendMessage(user,salon,msg,IfClientServerProtocol.Message);
								Style styleGP = ((StyledDocument)doc).getStyle(GRAY_PLAIN);
								Style styleBI = ((StyledDocument)doc).getStyle(BOLD_ITALIC);
								doc.insertString(doc.getLength(), BroadcastThread.clientTreadsMap.get(user).socket.getInetAddress().getHostAddress()+ " | "+user.getLogin() +" : ", styleBI);
								doc.insertString(doc.getLength(), msg+"\n", styleGP);
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
								ServeurLauncher.LoadTree();
							}
						}
					}
					else{
						doPost();
					}
				} 
				catch (IOException | BadLocationException ioe) {
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
