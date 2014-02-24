package com.PSI.controller;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;

import com.PSI.irc.server.ClientConnectThread;

public class ServeurLauncher {

	public static void main(String[] args) {
		
		StyledDocument styledDocument = new DefaultStyledDocument();
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		ClientConnectThread connect = new ClientConnectThread(4567,styledDocument,listModel);
		connect.run();
	}

}
