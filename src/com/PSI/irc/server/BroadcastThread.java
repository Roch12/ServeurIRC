package com.PSI.irc.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.PSI.controller.ServeurLauncher;
import com.PSI.irc.IfClientServerProtocol;

public class BroadcastThread extends Thread {
	
	/**
	 * Liste des utilisateurs et de leurs connexions
	 */
	public static HashMap<User, ServerToClientThread> clientTreadsMap=new HashMap<User, ServerToClientThread>();
	static{
		Collections.synchronizedMap(clientTreadsMap);
	}
	
	/**
	 * Liste des utilisateurs et de leurs salons
	 */
	public static HashMap<User, String> SalonMap=new HashMap<User, String>();
	static{
		Collections.synchronizedMap(SalonMap);
	}
	
	/**
	 * Ajouter un client au serveur
	 * @param user
	 * @param serverToClientThread
	 * @return boolean
	 */
	public static boolean addClient(User user, ServerToClientThread serverToClientThread){
		boolean res=true;
		if(clientTreadsMap.containsKey(user)){
			res=false;
		}
		else{
			clientTreadsMap.put(user, serverToClientThread);
			SalonMap.put(user,"Salon principal");
			sendMessage(user,"Salon principal", "", IfClientServerProtocol.ADD);
			ServeurLauncher.loadTree();
		}
		return res;
	}

	/**
	 * Envoyer un message
	 * @param sender
	 * @param salon
	 * @param msg
	 * @param action
	 */
	public static void sendMessage(User sender,String salon, String msg, String action){
		Collection<ServerToClientThread> clientTreads=clientTreadsMap.values();
		Iterator<ServerToClientThread> receiverClientThreadIterator=clientTreads.iterator();
		//pour chaque utilisateur connecté
		while (receiverClientThreadIterator.hasNext()) {
			ServerToClientThread clientThread = (ServerToClientThread) receiverClientThreadIterator.next();
			//si un utilisateur se déconnecte
			if(action.equals(IfClientServerProtocol.DEL))clientThread.post("#-#"+sender.getLogin()+"#"+salon);
			//si un utilisateur se connecte
			else if(action.equals(IfClientServerProtocol.ADD)) clientThread.post("#+#"+sender.getLogin()+"#" +salon);
			//si un utilisateur envoie un message sur le salon
			else clientThread.post("#S#"+sender.getLogin()+"#"+salon+"#"+msg);
			System.out.println("sendMessage : "+"#S#"+sender.getLogin()+"#"+salon+"#"+msg);
		}
		
	}
	
	/**
	 * Envoyer un message privé
	 * @param sender
	 * @param receveir
	 * @param msg
	 */
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
	
	/**
	 * Envoyé tous les utilisateurs connectés à la première authentification
	 * @param salon
	 */
	public static void loadUserByMessage(String salon){
		for (User user : clientTreadsMap.keySet()) {
			if(SalonMap.get(user).startsWith(salon)){
			sendMessage(user, SalonMap.get(user) ,"", IfClientServerProtocol.ADD);
			System.out.println("Reload " + SalonMap.get(user) + " pour " + user.getLogin());
			}
		}
	}
	
	/**
	 * Envoyer un message d'ajout/suppresion de salon
	 * @param salonName
	 * @param msgType
	 */
	public static void sendGestionSalon(String salonName,String msgType){
		Collection<ServerToClientThread> clientTreads=clientTreadsMap.values();
		Iterator<ServerToClientThread> receiverClientThreadIterator=clientTreads.iterator();
		while (receiverClientThreadIterator.hasNext()) {
			ServerToClientThread clientThread = (ServerToClientThread) receiverClientThreadIterator.next();
			clientThread.post(msgType+salonName);
		}
	}
	
	/**
	 * Envoyé au client la liste des salons existant lors de la première connection
	 * @param client
	 */
	public static void loadAllSalons(ServerToClientThread client){
		if(ServeurLauncher.listSalons.size() > 1){
		String msgToPost = "#SA";
		for (String salon : ServeurLauncher.listSalons) {
			if(!salon.equals("Salon principal")){
				msgToPost += "#" + salon;
			}
		}
		client.post(msgToPost);
		}
	}
	
	/**
	 * Récupérer la liste des utilisateurs d'un salon
	 * @param salon
	 * @return ArrayList<User>
	 */
	public static ArrayList<User> getUsersBySalon(String salon){
		ArrayList<User> listUsers = new ArrayList<User>();
		for (User user : SalonMap.keySet()) {
			if(SalonMap.get(user).equals(salon))
			{
				listUsers.add(user);
			}
		}
		
		return listUsers;
	}
	
	/**
	 * Récuperer un utilisateur en fonction de son nom
	 * @param username
	 * @return User
	 */
	public static User getUserByName(String username){
		for (User user : clientTreadsMap.keySet()) {
			if(user.getLogin().equals(username)) return user;
		}
		return null;
	}
	
	/**
	 * Supprimer un utilisateur du serveur
	 * @param user
	 */
	public static void removeClient(User user){
		clientTreadsMap.remove(user);
		SalonMap.remove(user);
	}
	
	/**
	 * Accepter un utilisateur sur le serveur
	 * @param user
	 * @return boolean
	 */ 
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
