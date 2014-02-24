package com.mroch.view;
import java.awt.BorderLayout;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Document;
import javax.swing.JSplitPane;
import javax.swing.JList;

import java.awt.Dimension;

import javax.swing.AbstractListModel;
import javax.swing.JTextArea;

import java.awt.Color;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;


public class ClientIRCWindow extends JFrame {
	private JPanel contentPane;
	private JTextField textField;
	private JList<String> list;

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
		
		
		list = new JList<String>(listModel);

		list.setPreferredSize(new Dimension(100, 0));
		splitPane.setLeftComponent(list);
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		

		
		JTextArea textArea = new JTextArea(documentModel);
		panel.add(textArea);
		textArea.setEditable(false);
		textArea.setBackground(Color.WHITE);
		
		textField = new JTextField(userInput,null,0);

		panel.add(textField, BorderLayout.SOUTH);
		textField.setColumns(10);
	}

	public JTextField getTextField() {
		return textField;
	}
	public JList getList() {
		return list;
	}
}
