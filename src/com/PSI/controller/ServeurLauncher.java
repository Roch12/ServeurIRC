package com.PSI.controller;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.PSI.irc.IfClientServerProtocol;
import com.PSI.irc.server.BroadcastThread;
import com.PSI.irc.server.ClientConnectThread;
import com.PSI.irc.server.User;
import com.mroch.view.AddSalonWindow;
import com.mroch.view.ServerIRCWindow;

public class ServeurLauncher {

	public static ServerIRCWindow frame;
	public static ArrayList<String> listSalons;
	public static DefaultStyledDocument listModel;
	
	/**
	 * Launch Server Application
	 * @param args
	 */
	public static void main(String[] args) {
		listSalons= new ArrayList<String>();
		listSalons.add("Salon principal");
		
		StyledDocument styledDocument = new DefaultStyledDocument();
		DefaultStyledDocument docChat = new DefaultStyledDocument();
		listModel = new DefaultStyledDocument();
		
		//création de la connexion Serveur
		ClientConnectThread connect = new ClientConnectThread(4567,styledDocument, docChat);
		frame = new ServerIRCWindow(styledDocument, listModel, docChat);
		
		//chargement du JTree
		loadTree();
		frame.setVisible(true);
		
		//Ajout d'un salon sur le serveur et les clients
		frame.getBtnAjouter().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				final AddSalonWindow addSalonDialog = new AddSalonWindow();
				addSalonDialog.setVisible(true);
				addSalonDialog.getAjouterSalonButton().addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0){
						if(!addSalonDialog.getTxtSalonName().getText().isEmpty()){
							listSalons.add(addSalonDialog.getTxtSalonName().getText());
							BroadcastThread.sendGestionSalon(addSalonDialog.getTxtSalonName().getText(), IfClientServerProtocol.AddSalon);
							loadTree();
							addSalonDialog.dispose();
						}
					}
				});
				addSalonDialog.getCancelButton().addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						addSalonDialog.dispose();
					}
				});
			}
		});
		
		
		//Supression d'un salon sur le serveur et sur les clients
		frame.getBtnSupprimer().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) frame.getTree().getLastSelectedPathComponent();
				if (node == null)
				    //Nothing is selected.  
				    return;
					
					//Récuperation de l'utilisateur si il existe
				    String nodeInfo = (String)node.getUserObject();
				    if(listSalons.contains(nodeInfo)){
				    	if(BroadcastThread.getUsersBySalon(nodeInfo).size() == 0){
				    		if(nodeInfo != "Salon principal"){
				    	listSalons.remove(nodeInfo);
				    	BroadcastThread.sendGestionSalon(nodeInfo, IfClientServerProtocol.removeSalon);
				    	loadTree();
				    	}
				    		else JOptionPane.showMessageDialog(frame, "Impossible de supprimer le salon principal");    	
							   
				    	}
				    	else JOptionPane.showMessageDialog(frame, "Veuillez attendre qu'il n'y ait plus d'utilisateurs avant de supprimer ce salon");    	
				    }
			    	else JOptionPane.showMessageDialog(frame, "Veuillez selectionner un salon");
			}
		});
		
		//Event au click sur une node
		frame.getTree().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				System.out.println("ValueChange on Tree");
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) frame.getTree().getLastSelectedPathComponent();
				if (node == null)
				    //Nothing is selected.  
				    return;
					
					//Récuperation de l'utilisateur si il existe
				    String nodeInfo = (String)node.getUserObject();
				    User user = BroadcastThread.getUserByName(nodeInfo);
				    
				    //Affichage des informations de l'utilisateurs
				    if(user!=null){
				    	//listModel = new DefaultStyledDocument();
				    	try {
				    		listModel.remove(0, listModel.getLength());
							listModel.insertString(listModel.getLength(), "Username : " + user.getLogin() + "\n", null);
							listModel.insertString(listModel.getLength(), "IP Adresse : " + BroadcastThread.clientTreadsMap.get(user).socket.getInetAddress().getHostAddress()+ "\n", null);
							listModel.insertString(listModel.getLength(), "Nom de la machine : " + BroadcastThread.clientTreadsMap.get(user).socket.getInetAddress().getHostName()+ "\n", null);
							listModel.insertString(listModel.getLength(), "Temps d'utilisation : " + millisToString(BroadcastThread.clientTreadsMap.get(user).time) + "\n", null);
							System.out.println("User is charged");
				    	} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
				    } 
				    else{
				    	try {
							listModel.remove(0, listModel.getLength());
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
				    	System.out.println("User is null");
				    }
			}
		});
		
		connect.start();
	}

	/**
	 * Création des nodes du JTree en fonction des utilisateurs dans leurs salons
	 */
	public static void loadTree(){
		frame.getTree().setModel(new DefaultTreeModel(
				new DefaultMutableTreeNode("Liste des salons") {
					{
						DefaultMutableTreeNode node_1;
						for (String salon : listSalons) {
							node_1 = new DefaultMutableTreeNode(salon);
							for (User user : BroadcastThread.getUsersBySalon(salon)) {
								node_1.add(new DefaultMutableTreeNode(user.getLogin()));
							}
						add(node_1);
						}
						
					}
				}
			));
		frame.getTree().repaint();
	}
	
	/**
	 * Récuperer l'affichage String "HH:MM:SS" à partir de millisecondes
	 * @param millis
	 * @return String
	 */
	public static String millisToString(long millis)
	{
		
		long seconds = (System.currentTimeMillis() - millis) / 1000;
		long numhours = (long) Math.floor(((seconds % 31536000) % 86400) / 3600);
		long numminutes = (long) Math.floor((((seconds % 31536000) % 86400) % 3600) / 60);
		long numseconds = (((seconds % 31536000) % 86400) % 3600) % 60;
	return numhours + " h " + numminutes + " m " + numseconds + " s";

	}
}
