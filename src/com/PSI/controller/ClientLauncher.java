package com.PSI.controller;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

import com.PSI.irc.client.ClientToServerThread;
import com.PSI.irc.server.ServerToClientThread;
import com.PSI.irc.server.User;
import com.mroch.view.ClientIRCWindow;
import com.mroch.view.CreateDialogConnection;

public class ClientLauncher {

	public static Boolean error = false;
	public static final DefaultStyledDocument documentModel = new DefaultStyledDocument();
	public static final DefaultStyledDocument userInput = new DefaultStyledDocument();
	public static DefaultListModel<String> listModel = new DefaultListModel<String>();
	public static HashMap<String, StyledDocument> listDocuments=new HashMap<String, StyledDocument>();
	static{
		Collections.synchronizedMap(listDocuments);
	}
	
	public static int tabSelected = 0;
	public static  ClientIRCWindow frame;
	public static void main(String[] args) {
				listDocuments.put("Salon Principal",documentModel);
				final CreateDialogConnection dialogConnection = new CreateDialogConnection();
				dialogConnection.setDefaultCloseOperation(dialogConnection.DISPOSE_ON_CLOSE);
				dialogConnection.setVisible(true);
				if(error) JOptionPane.showMessageDialog(dialogConnection, "Le Username est déjà utilisé, veuillez réessayer avec un nouveau !");
				dialogConnection.getOkButton().addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						if(!dialogConnection.getTxtLocalhost().getText().isEmpty() && 
								!dialogConnection.getTxtAnonymous().getText().isEmpty() && 
								!dialogConnection.getTextField_1().getText().isEmpty() && 
								!dialogConnection.getTextField_3().getText().isEmpty())
						{
							try {
								launchSocket(dialogConnection.getTxtLocalhost().getText(),
										Integer.parseInt(dialogConnection.getTextField_1().getText()),
										dialogConnection.getTxtAnonymous().getText(),
										dialogConnection.getTextField_3().getText());
								dialogConnection.dispose();
							} catch (IOException e) {
								JOptionPane.showMessageDialog(dialogConnection, "Le serveur que vous avez demandé est actuellement indisponible, \n Veuillez vérifier votre adresse de connection et réessayer ultérieurement.");
							}
							catch(NumberFormatException e)
							{
								JOptionPane.showMessageDialog(dialogConnection, "Veuillez vérifier le port de connexion !");
							}
						}
						else
						{
							JOptionPane.showMessageDialog(dialogConnection, "Erreur, Veuillez remplir tout les champs !");
						}
					}
				});
				
		}
		
	public static void launchSocket(String adresse, int port,String username, String mdp) throws IOException
	{
		Socket socket = null;
		socket = new Socket(adresse, port);
		final ClientToServerThread client = new ClientToServerThread(documentModel, listModel, socket, username, mdp);

		client.open();
		client.start();
	}
	public static void launchClient( final ClientToServerThread client)
	{
		frame = new ClientIRCWindow(documentModel, userInput, listModel);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {
					client.quitServer();
					client.close();
				} catch (IOException e) {
				
					e.printStackTrace();
				}
			}
		});
		
		frame.getTabbedPane().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				tabSelected = frame.getTabbedPane().getSelectedIndex();
				System.out.println("change tab : " + tabSelected);
				
			}
		});
		
		frame.getList().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				boolean res = true;
				for (int i = 0; i < frame.getTabbedPane().countComponents(); i++) {
					if(frame.getTabbedPane().getTitleAt(i) == listModel.get(frame.getList().getSelectedIndex()) || frame.getList().getSelectedIndex() == 0 )
						res = false;
				}
				if(res){ 
					int index = frame.getList().getSelectedIndex();
					listDocuments.put(listModel.get(index),new DefaultStyledDocument());
					System.out.println("Add Tab : / nb tab = " + listDocuments.size());
					frame.AddPrivateUserTab(listModel.get(index), listDocuments.get(listModel.get(index)));
					
				}
			}
		});
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
	}

}
