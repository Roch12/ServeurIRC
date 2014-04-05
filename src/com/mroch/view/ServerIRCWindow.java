package com.mroch.view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;

public class ServerIRCWindow extends JFrame {

	private JPanel contentPane;
	private JTree tree;

	/**
	 * Create the frame.
	 */
	public ServerIRCWindow(StyledDocument doc, DefaultStyledDocument docInformation, DefaultStyledDocument docChat) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1050, 608);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JSplitPane splitPane2 = new JSplitPane();
		splitPane2.setOrientation(splitPane2.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane2);
		
		
		JPanel panel3 = new JPanel();
		panel3.setBorder(new TitledBorder(null, "Connection Log", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel3.setPreferredSize(new Dimension(10, 250));
		panel3.setLayout(new BorderLayout(0, 0));
		
		

		JTextArea textAreaMsg = new JTextArea(doc);
		textAreaMsg.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textAreaMsg.setEditable(false);
		
		JScrollPane scroll = new JScrollPane();
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setViewportView(textAreaMsg);
		DefaultCaret caret = (DefaultCaret)textAreaMsg.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		panel3.add(scroll);
		splitPane2.setTopComponent(panel3);
		//splitPane.setRightComponent(textArea);
		
		JPanel panel4 = new JPanel();
		panel4.setPreferredSize(new Dimension(10, 250));
		panel4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Chat Log", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel4.setLayout(new BorderLayout(0, 0));
		
		
		JTextArea textAreaConnection = new JTextArea(docChat);
		textAreaConnection.setEditable(false);
		
		JScrollPane scroll2 = new JScrollPane();
		scroll2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll2.setViewportView(textAreaConnection);
		DefaultCaret caret2 = (DefaultCaret)textAreaConnection.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		
		panel4.add(scroll2);
		splitPane2.setBottomComponent(panel4);
		
		JPanel panel_1 = new JPanel();
		//contentPane.add(panel_1, BorderLayout.NORTH);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Salons", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setLayout(new BorderLayout(0, 0));
		splitPane.setLeftComponent(panel_1);
		
		tree = new JTree();

		tree.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
		panel_1.add(tree);
		tree.setMaximumSize(new Dimension(200, 64));
		tree.setPreferredSize(new Dimension(173, 450));
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(250, 10));
		contentPane.add(panel, BorderLayout.EAST);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel0 = new JPanel();
		panel.add(panel0);
		panel0.setPreferredSize(new Dimension(230, 10));
		panel0.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Informations", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel0.setLayout(new BorderLayout(0, 0));
		
		JTextArea textArea = new JTextArea(docInformation);
		panel0.add(textArea, BorderLayout.CENTER);
	}

	public JTree getTree() {
		return tree;
	}
}
