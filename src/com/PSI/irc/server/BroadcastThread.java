package com.PSI.irc.server;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import com.PSI.irc.IfClientServerProtocol;

public class BroadcastThread extends Thread {
	
	public static HashMap<User, ServerToClientThread> clientTreadsMap=new HashMap<User, ServerToClientThread>();
	static{
		Collections.synchronizedMap(clientTreadsMap);
	}
	
	public static HashMap<User, String> SalonMap=new HashMap<User, String>();
	static{
		Collections.synchronizedMap(SalonMap);
	}
	
	public static boolean addClient(User user, ServerToClientThread serverToClientThread){
		boolean res=true;
		if(clientTreadsMap.containsKey(user)){
			res=false;
		}
		else{
			clientTreadsMap.put(user, serverToClientThread);
			SalonMap.put(user,"Salon principal");
			sendMessage(user,"Salon principal", "", IfClientServerProtocol.ADD);
		}
		return res;
	}

	public static void sendMessage(User sender,String salon, String msg, String action){
		Collection<ServerToClientThread> clientTreads=clientTreadsMap.values();
		Iterator<ServerToClientThread> receiverClientThreadIterator=clientTreads.iterator();
		while (receiverClientThreadIterator.hasNext()) {
			ServerToClientThread clientThread = (ServerToClientThread) receiverClientThreadIterator.next();
			if(action.equals(IfClientServerProtocol.DEL))clientThread.post("#-#"+sender.getLogin()+"#"+salon);
			else if(action.equals(IfClientServerProtocol.ADD)) clientThread.post("#+#"+sender.getLogin()+"#" +salon);
			else clientThread.post("#S#"+sender.getLogin()+"#"+salon+"#"+msg);
			System.out.println("sendMessage : "+"#S#"+sender.getLogin()+"#"+salon+"#"+msg);
		}
		
	}
	
	public static void sendPrivateMessage(User sender, String receveir, String msg){
		for (User user : clientTreadsMap.keySet()) {
			if(user.getLogin().equals(receveir))
			{
				clientTreadsMap.get(user).post("#W#"+sender.getLogin()+"#"+msg);
				System.out.println("#W#"+sender.getLogin()+"#"+msg);
				break;
			}
		}
	}
	
	public static void loadUserByMessage(String salon){
		Collection<ServerToClientThread> clientTreads=clientTreadsMap.values();
		Iterator<ServerToClientThread> receiverClientThreadIterator=clientTreads.iterator();
		for (User user : clientTreadsMap.keySet()) {
			if(SalonMap.get(user).startsWith(salon)){
			sendMessage(user, SalonMap.get(user) ,"", IfClientServerProtocol.ADD);
			System.out.println("Reload " + SalonMap.get(user) + " pour " + user.getLogin());
			}
		}
	}
	
	public static void removeClient(User user){
		clientTreadsMap.remove(user);
		SalonMap.remove(user);
	}
	
	public static boolean accept(User user){
		boolean res=true;
		if(clientTreadsMap.containsKey(user)){
			res= false;
		}
		else{
			for (User user1 : clientTreadsMap.keySet()) {
				if(user1.getLogin().equals(user.getLogin()))
				{
					res = false;
				}
			}
		}
		return res;
	}
}
