package com.PSI.controller;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
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
	public static DefaultStyledDocument documentModel = new DefaultStyledDocument();
	public static final DefaultStyledDocument userInput = new DefaultStyledDocument();
	public static DefaultListModel<String> listModel = new DefaultListModel<String>();
	public static HashMap<String, StyledDocument> listDocuments=new HashMap<String, StyledDocument>();
	public static ArrayList<String> listSalon = new ArrayList<String>();
	public static String SalonName = "Salon principal";
	static{
		Collections.synchronizedMap(listDocuments);
		listSalon.add("Salon principal");
	}
	
	public static int tabSelected = 0;
	public static  ClientIRCWindow frame;
	
	/**
	 * Launch the Client Application
	 * @param args
	 */
	public static void main(String[] args) {
				final CreateDialogConnection dialogConnection = new CreateDialogConnection();
				dialogConnection.setDefaultCloseOperation(dialogConnection.DISPOSE_ON_CLOSE);
				dialogConnection.setVisible(true);
				if(error) JOptionPane.showMessageDialog(dialogConnection, "Le Username est déjà utilisé, veuillez réessayer avec un nouveau !");
				dialogConnection.getOkButton().addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						//vérification des champs
						if(!dialogConnection.getTxtLocalhost().getText().isEmpty() && 
								!dialogConnection.getTxtAnonymous().getText().isEmpty() && 
								!dialogConnection.getTxtPort().getText().isEmpty() && 
								!dialogConnection.getTxtPassword().getText().isEmpty())
						{
							try {
								//création du socket pour la connexion
								launchSocket(dialogConnection.getTxtLocalhost().getText(),
										Integer.parseInt(dialogConnection.getTxtPort().getText()),
										dialogConnection.getTxtAnonymous().getText(),
										dialogConnection.getTxtPassword().getText());
								dialogConnection.dispose();
							} catch (IOException e) {
								//Erreur en cas de serveur indisponible
								JOptionPane.showMessageDialog(dialogConnection, "Le serveur que vous avez demandé est actuellement indisponible, \n Veuillez vérifier votre adresse de connection et réessayer ultérieurement.");
							}
							catch(NumberFormatException e)
							{
								//Erreur en cas de mauvaise écriture du port
								JOptionPane.showMessageDialog(dialogConnection, "Veuillez vérifier le port de connexion !");
							}
						}
						else
						{
							//Erreur en cas de non remplissage des champs
							JOptionPane.showMessageDialog(dialogConnection, "Erreur, Veuillez remplir tout les champs !");
						}
					}
				});
				
				dialogConnection.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0){
						dialogConnection.dispose();
					}
				});
				
		}
		
	/**
	 * Launch Socket and create ClientToServerThread
	 * @param adresse
	 * @param port
	 * @param username
	 * @param mdp
	 * @throws IOException
	 */
	public static void launchSocket(String adresse, int port,String username, String mdp) throws IOException
	{
		Socket socket = null;
		socket = new Socket(adresse, port);
		final ClientToServerThread client = new ClientToServerThread(documentModel, listModel, socket, username, mdp);

		client.open();
		client.start();
	}
	
	/**
	 * Création de l'interface client
	 * @param client
	 */
	public static void launchClient( final ClientToServerThread client)
	{
		frame = new ClientIRCWindow(documentModel, userInput, listModel, listSalon);
		frame.getUserName().setText(client.login);
		frame.getNbusers().setText(client.nbUsers.toString());
		
		//Event en cas de fermeture de l'application
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

		//Event en cas de changement de tab
		frame.getTabbedPane().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				tabSelected = frame.getTabbedPane().getSelectedIndex();
				
				frame.getTabbedPane().setBackgroundAt(tabSelected, null);
				System.out.println("change tab : " + tabSelected);
				
			}
		});
		
		//Event en cas de click sur un utilisateur
		frame.getList().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(listModel.getSize() >0){
				boolean res = true;
				for (int i = 0; i < listModel.getSize(); i++) {
					if(frame.getTabbedPane().getTitleAt(i) == listModel.get(frame.getList().getSelectedIndex()) )
						res = false;
				}
				if(res){ 
					int index = frame.getList().getSelectedIndex();
					listDocuments.put(listModel.get(index),new DefaultStyledDocument());
					System.out.println("Add Tab : / nb tab = " + listDocuments.size());
					frame.AddPrivateUserTab(listModel.get(index), listDocuments.get(listModel.get(index)));
					
				}
				}
			}
		});
		
		//Event pour envoyer le texte quand on appuie sur la touche Entrée
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
		
		//Event en cas de click sur le bouton Changer de salon
		frame.getBtnChanger().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				listModel.clear();
				for (int i = 1; i < frame.getTabbedPane().getTabCount(); i++) {
						ClientLauncher.frame.getTabbedPane().setSelectedIndex(0);
						ClientLauncher.frame.getTabbedPane().remove(i);
						ClientLauncher.frame.getTabbedPane().repaint();
					}
				listDocuments.clear();
				client.ChangeSalon(SalonName, frame.getComboBoxSalon().getSelectedItem().toString());
				SalonName = frame.getComboBoxSalon().getSelectedItem().toString();
				System.out.println(SalonName);
				frame.getTabbedPane().setTitleAt(0, SalonName);
				documentModel = new DefaultStyledDocument();
				frame.getTextPaneSalon().setDocument(documentModel);
			}
		});
		frame.setVisible(true);
	}

}
