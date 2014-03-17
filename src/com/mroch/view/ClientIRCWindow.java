package com.mroch.view;
import java.awt.BorderLayout;
import java.awt.Color;
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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;


public class ClientIRCWindow extends JFrame {
	private JPanel contentPane;
	private JTextField textField;
	private JList<String> list;
	private JPanel panel_1;
	private JTabbedPane tabbedPane;
	private JPanel panel;

	/**
	 * Create the frame.
	 */
	public ClientIRCWindow(Document documentModel, Document userInput, ListModel<String> listModel) {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 636, 392);
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
		

		
		JTextArea textArea = new JTextArea(documentModel);
		JScrollPane scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setViewportView(textArea);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		tabbedPane.add("Salon Principal",scroll);
		panel.add(tabbedPane);
		textArea.setEditable(false);
		textArea.setBackground(Color.WHITE);
		
		
		
		textField = new JTextField(userInput,null,0);

		panel.add(textField, BorderLayout.SOUTH);
		textField.setColumns(10);
		
		panel_1 = new JPanel();
		panel_1.setSize(new Dimension(200, 0));
		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		
		list = new JList<String>(listModel);
		
		
	
		panel_1.add(list);
		
				list.setPreferredSize(new Dimension(100, 0));
				
	}

	public JTextField getTextField() {
		return textField;
	}
	public JList getList() {
		return list;
	}
	
	public void AddPrivateUserTab(String username,StyledDocument styledDocument)
	{
		JTextArea textArea = new JTextArea(styledDocument);
		JScrollPane scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setViewportView(textArea);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		tabbedPane.add(username,scroll);
		panel.add(tabbedPane);
		textArea.setEditable(false);
		textArea.setBackground(Color.WHITE);
	}
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
}


