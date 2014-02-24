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
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class ClientIRCWindow extends JFrame {
	private JPanel contentPane;
	private JTextField textField;
	private JList<String> list;
	private JPanel panel_1;

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
}
