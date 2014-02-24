package com.PSI.controller;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;
import javax.swing.text.DefaultStyledDocument;

import com.PSI.irc.client.ClientToServerThread;
import com.PSI.irc.client.SimpleChatClient;
import com.mroch.view.*;

public class ClientLauncher {

	public static void main(String[] args) {
		DefaultStyledDocument documentModel = new DefaultStyledDocument();
		DefaultStyledDocument userInput = new DefaultStyledDocument();
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		if (args.length != 2){
			System.out.println("Usage: java ChatClient host port");
		}
		else{
			Socket socket = null;
			try {
				socket = new Socket(args[0], Integer.parseInt(args[1]));
				ClientToServerThread client = new ClientToServerThread(documentModel, listModel, socket, "User", "User");
				ClientIRCWindow frame = new ClientIRCWindow(documentModel, userInput, listModel);
				frame.setVisible(true);
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}	
		}
		
	}

}
