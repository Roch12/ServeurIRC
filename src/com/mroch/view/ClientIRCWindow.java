package com.mroch.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabExpander;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JComboBox;
import javax.swing.JButton;


public class ClientIRCWindow extends JFrame {
	private JPanel contentPane;
	private JTextField textField;
	private JList<String> list;
	private JPanel panel_1;
	private JTabbedPane tabbedPane;
	private JPanel panel;
	private HashMap<Integer,Component> tabs;
	private JTextArea textAreaSalon;
	private JSplitPane splitPane_1;
	private JComboBox comboBoxSalon;
	private JButton btnChanger;

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
		

		
		textAreaSalon = new JTextArea(documentModel);
		JScrollPane scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setViewportView(textAreaSalon);
		DefaultCaret caret = (DefaultCaret)textAreaSalon.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		tabbedPane.add("Salon Principal",scroll);
		//tabbedPane.ge
		panel.add(tabbedPane);
		textAreaSalon.setEditable(false);
		textAreaSalon.setBackground(Color.WHITE);
		
		
		
		textField = new JTextField(userInput,null,0);

		panel.add(textField, BorderLayout.SOUTH);
		textField.setColumns(10);
		
		panel_1 = new JPanel();
		panel_1.setSize(new Dimension(200, 0));
		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		
		list = new JList<String>(listModel);
		
		
	
		panel_1.add(list);
		
				list.setPreferredSize(new Dimension(200, 0));
				
				splitPane_1 = new JSplitPane();
				splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
				panel_1.add(splitPane_1, BorderLayout.SOUTH);
				
				comboBoxSalon = new JComboBox(listSalon.toArray());
				splitPane_1.setLeftComponent(comboBoxSalon);
				
				btnChanger = new JButton("Changer de salon");

				splitPane_1.setRightComponent(btnChanger);
				
	}

	public JTextField getTextField() {
		return textField;
	}
	public JList getList() {
		return list;
	}
	
	public HashMap<Integer,Component> getTabsList(){
		return tabs;
	}
	
	public void AddPrivateUserTab(String username,StyledDocument styledDocument)
	{
		JTextArea textArea = new JTextArea(styledDocument);
		JScrollPane scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setViewportView(textArea);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		Component c = tabbedPane.add(username,scroll);
		
		tabs.put(tabbedPane.getTabCount(),c);
		panel.add(tabbedPane);
		textArea.setEditable(false);
		textArea.setBackground(Color.WHITE);
	}
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
	public JTextArea getTextAreaSalon() {
		return textAreaSalon;
	}
	public JButton getBtnChanger() {
		return btnChanger;
	}
	public JComboBox getComboBoxSalon() {
		return comboBoxSalon;
	}
}


