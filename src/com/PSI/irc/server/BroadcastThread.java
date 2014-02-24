package com.PSI.irc.server;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.PSI.irc.IfClientServerProtocol;

public class BroadcastThread extends Thread {
	
	public static HashMap<User, ServerToClientThread> clientTreadsMap=new HashMap<User, ServerToClientThread>();
	static{
		Collections.synchronizedMap(clientTreadsMap);
	}
	
	public static boolean addClient(User user, ServerToClientThread serverToClientThread){
		boolean res=true;
		if(clientTreadsMap.containsKey(user)){
			res=false;
		}
		else{
			clientTreadsMap.put(user, serverToClientThread);
			sendMessage(user, "", IfClientServerProtocol.ADD);
		}
		return res;
	}

	public static void sendMessage(User sender, String msg, String action){
		Collection<ServerToClientThread> clientTreads=clientTreadsMap.values();
		Iterator<ServerToClientThread> receiverClientThreadIterator=clientTreads.iterator();
		while (receiverClientThreadIterator.hasNext()) {
			ServerToClientThread clientThread = (ServerToClientThread) receiverClientThreadIterator.next();
			if(action.equals(IfClientServerProtocol.DEL))clientThread.post("#-#"+sender.getLogin());
			else if(action.equals(IfClientServerProtocol.ADD)) clientThread.post("#+#"+sender.getLogin());
			else clientThread.post("#"+sender.getLogin()+"#"+msg);
			System.out.println("sendMessage : "+"#"+sender.getLogin()+"#"+msg);
		}
	}
	
	public static void loadUserByMessage(){
		Collection<ServerToClientThread> clientTreads=clientTreadsMap.values();
		Iterator<ServerToClientThread> receiverClientThreadIterator=clientTreads.iterator();
		for (User user : clientTreadsMap.keySet()) {
			sendMessage(user, "", IfClientServerProtocol.ADD);
		}
	}
	
	public static void removeClient(User user){
		clientTreadsMap.remove(user);
	}
	
	public static boolean accept(User user){
		boolean res=true;
		if(clientTreadsMap.containsKey(user)){
			res= false;
		}
		return res;
	}
}
