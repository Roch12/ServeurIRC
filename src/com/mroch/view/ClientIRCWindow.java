package com.mroch.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;



public class ClientIRCWindow extends JFrame {
	
	/**
	 * Liste Paramètres pour la création de l'interface client
	 */
	private JPanel contentPane;
	private JTextField sendTextField;
	private JList<String> userList;
	private JPanel usersPanel;
	private JTabbedPane tabbedPane;
	private JPanel panel;
	private HashMap<Integer,Component> tabs;
	private JTextPane textPaneSalon;
	private JSplitPane SalonSplitPane;
	private JComboBox comboBoxSalon;
	private JButton btnChanger;
	private JSplitPane usersInfoSplitPane;
	private JPanel userInfoPanel;
	private JPanel nbUserPanel;
	private JLabel userImg;
	private JLabel userName;
	private JLabel lblNbUtilisateurs;
	private JLabel lblNbusers;

	/**
	 * Create the frame.
	 */
	public ClientIRCWindow(Document documentModel, Document userInput, ListModel<String> listModel, ArrayList<String> listSalon) {

		tabs = new HashMap<Integer,Component>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1028, 628);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel.add(tabbedPane, BorderLayout.NORTH);
		

		
		textPaneSalon = new JTextPane((StyledDocument) documentModel);
		JScrollPane scroll = new JScrollPane();
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setViewportView(textPaneSalon);
		DefaultCaret caret = (DefaultCaret)textPaneSalon.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		tabbedPane.add("Salon Principal",scroll);
		panel.add(tabbedPane);
		textPaneSalon.setEditable(false);
		textPaneSalon.setBackground(Color.WHITE);
		
		
		
		sendTextField = new JTextField(userInput,null,0);

		panel.add(sendTextField, BorderLayout.SOUTH);
		sendTextField.setColumns(10);
		
		usersPanel = new JPanel();
		usersPanel.setSize(new Dimension(200, 0));
		splitPane.setLeftComponent(usersPanel);
		usersPanel.setLayout(new BorderLayout(0, 0));
		
		
		userList = new JList<String>(listModel);
		
		
	
		usersPanel.add(userList);
		
				userList.setPreferredSize(new Dimension(200, 0));
				
				SalonSplitPane = new JSplitPane();
				SalonSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
				usersPanel.add(SalonSplitPane, BorderLayout.SOUTH);
				
				comboBoxSalon = new JComboBox();
				comboBoxSalon.setModel(new DefaultComboBoxModel(listSalon.toArray()));
				SalonSplitPane.setLeftComponent(comboBoxSalon);
				
				btnChanger = new JButton("Changer de salon");

				SalonSplitPane.setRightComponent(btnChanger);
				
				usersInfoSplitPane = new JSplitPane();
				usersInfoSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
				usersPanel.add(usersInfoSplitPane, BorderLayout.NORTH);
				
				userInfoPanel = new JPanel();
				userInfoPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
				usersInfoSplitPane.setLeftComponent(userInfoPanel);
				
				
				userImg = new JLabel(new ImageIcon(this.getClass().getResource("/user.png")));
				userImg.setHorizontalTextPosition(SwingConstants.LEFT);
				userImg.setPreferredSize(new Dimension(70, 70));
				userImg.setHorizontalAlignment(SwingConstants.LEFT);
				userInfoPanel.add(userImg);
				
				userName = new JLabel("");
				userInfoPanel.add(userName);
				
				nbUserPanel = new JPanel();
				usersInfoSplitPane.setRightComponent(nbUserPanel);
				
				lblNbUtilisateurs = new JLabel("Nb utilisateurs :");
				nbUserPanel.add(lblNbUtilisateurs);
				
				lblNbusers = new JLabel("0");
				nbUserPanel.add(lblNbusers);
				
	}

	/**
	 * Récupérer le textfield permettant d'envoyer des messages
	 * @return JTextField
	 */
	public JTextField getTextField() {
		return sendTextField;
	}
	
	/**
	 * Récuperer la liste des utilisateurs
	 * @return JList
	 */
	public JList getList() {
		return userList;
	}
	
	
	
	/**
	 * Ajouter une tab en cas de discussion privée
	 * @param username
	 * @param styledDocument
	 */
	public void AddPrivateUserTab(String username,StyledDocument styledDocument)
	{
		JTextPane textPane = new JTextPane(styledDocument);
		JScrollPane scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setViewportView(textPane);
		DefaultCaret caret = (DefaultCaret)textPane.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		Component c = tabbedPane.add(username,scroll);
		
		tabs.put(tabbedPane.getTabCount(),c);
		panel.add(tabbedPane);
		textPane.setEditable(false);
		textPane.setBackground(Color.WHITE);
	}
	
	/**
	 * Récuperer le component qui gère les tabs
	 * @return JTabbedPane
	 */
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
	
	/**
	 * Récuperer le TextPane qui affiche les messages
	 * @return JTextPane
	 */
	public JTextPane getTextPaneSalon() {
		return textPaneSalon;
	}
	
	/**
	 * Récuperer le bouton pour changer de salon
	 * @return JButton
	 */
	public JButton getBtnChanger() {
		return btnChanger;
	}
	
	/**
	 * Récuperer la Combobox des salons
	 * @return JComboBox
	 */
	public JComboBox getComboBoxSalon() {
		return comboBoxSalon;
	}
	
	/**
	 * Récuperer le label qui affiche l'image de l'utilisateur
	 * @return JLabel
	 */
	public JLabel getUserImg() {
		return userImg;
	}
	
	/**
	 * Récupèrer le label qui affiche le pseudo de l'utilisateur
	 * @return JLabel
	 */
	public JLabel getUserName() {
		return userName;
	}
	
	/**
	 * Récupèrer le label qui affiche le nombre d'utilisateurs dans le salon
	 * @return JLabel
	 */
	public JLabel getNbusers() {
		return lblNbusers;
	}
	
	/**
	 * Mise a jour de la JcomboBox des salons
	 * @param listSalon
	 */
	public void setComboBoxSalon(ArrayList<String> listSalon){
		comboBoxSalon.setModel(new DefaultComboBoxModel(listSalon.toArray()));
		comboBoxSalon.repaint();
	}
}


