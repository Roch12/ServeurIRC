package com.PSI.controller;
import java.util.ArrayList;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.PSI.irc.server.BroadcastThread;
import com.PSI.irc.server.ClientConnectThread;
import com.PSI.irc.server.User;
import com.mroch.view.ServerIRCWindow;

public class ServeurLauncher {

	public static ServerIRCWindow frame;
	public static ArrayList<String> listSalons;
	public static DefaultStyledDocument listModel;
	
	public static void main(String[] args) {
		listSalons= new ArrayList<String>();
		listSalons.add("Salon principal");
		listSalons.add("Salon Secondaire");
		listSalons.add("Salon Terciaire");
		
		StyledDocument styledDocument = new DefaultStyledDocument();
		DefaultStyledDocument docChat = new DefaultStyledDocument();
		listModel = new DefaultStyledDocument();
		ClientConnectThread connect = new ClientConnectThread(4567,styledDocument, docChat);
		frame = new ServerIRCWindow(styledDocument, listModel, docChat);
		loadTree();
		frame.setVisible(true);
		
		frame.getTree().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				System.out.println("ValueChange on Tree");
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) frame.getTree().getLastSelectedPathComponent();
				if (node == null)
				    //Nothing is selected.  
				    return;
					
				    String nodeInfo = (String)node.getUserObject();
				    User user = BroadcastThread.getUserByName(nodeInfo);
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
	
	public static String millisToString(long millis)
	{
		
		long seconds = (System.currentTimeMillis() - millis) / 1000;
		long numhours = (long) Math.floor(((seconds % 31536000) % 86400) / 3600);
		long numminutes = (long) Math.floor((((seconds % 31536000) % 86400) % 3600) / 60);
		long numseconds = (((seconds % 31536000) % 86400) % 3600) % 60;
	return numhours + " h " + numminutes + " m " + numseconds + " s";

	}
}
