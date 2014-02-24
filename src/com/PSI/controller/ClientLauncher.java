package com.PSI.controller;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

import com.PSI.irc.client.ClientToServerThread;
import com.PSI.irc.client.SimpleChatClient;
import com.mroch.view.*;

public class ClientLauncher {

	public static void main(String[] args) {
		final DefaultStyledDocument documentModel = new DefaultStyledDocument();
		final DefaultStyledDocument userInput = new DefaultStyledDocument();
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		if (args.length != 2){
			System.out.println("Usage: java ChatClient host port");
		}
		else{
			Socket socket = null;
			try {
				socket = new Socket(args[0], Integer.parseInt(args[1]));
				final ClientToServerThread client = new ClientToServerThread(documentModel, listModel, socket, "Maxime", "User");
				ClientIRCWindow frame = new ClientIRCWindow(documentModel, userInput, listModel);
				frame.getTextField().addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent arg0) {
						if(arg0.getKeyCode() == 10){
							String line = "";
							try {
								line = userInput.getText(0, userInput.getLength());
								if(line.equals("")){
								}
								else
								{
									client.setMsgToSend(line);
									userInput.remove(0, userInput.getLength());
									
								}
							} catch (BadLocationException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}
				});
				frame.setVisible(true);
				client.open();
				client.start();
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}	
		}
		
	}

}
