package com.PSI.controller;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
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
	
	public static void main(String[] args) {
		listSalons= new ArrayList<String>();
		listSalons.add("Salon principal");
		listSalons.add("Salon Secondaire");
		listSalons.add("Salon Terciaire");
		
		StyledDocument styledDocument = new DefaultStyledDocument();
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		ClientConnectThread connect = new ClientConnectThread(4567,styledDocument,listModel);
		frame = new ServerIRCWindow(styledDocument, listModel);
		LoadTree();
		frame.setVisible(true);
		
		connect.start();
	}

	public static void LoadTree(){
		frame.getTree().setModel(new DefaultTreeModel(
				new DefaultMutableTreeNode("Liste des salons") {
					{
						DefaultMutableTreeNode node_1;
						for (String salon : listSalons) {
							node_1 = new DefaultMutableTreeNode(salon);
							for (User user : BroadcastThread.GetUsersBySalon(salon)) {
								node_1.add(new DefaultMutableTreeNode(user.getLogin()));
							}
						add(node_1);
						}
						
					}
				}
			));
		frame.getTree().repaint();
	}
}
