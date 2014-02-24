package com.mroch.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.StyledDocument;

public class ServerIRCWindow extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public ServerIRCWindow(StyledDocument doc, DefaultListModel<String> listModel) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 967, 608);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JList<String> list = new JList<String>(listModel);
		list.setPreferredSize(new Dimension(200, 0));
		splitPane.setLeftComponent(list);
		
		JTextArea textArea = new JTextArea(doc);
		splitPane.setRightComponent(textArea);
	}

}
