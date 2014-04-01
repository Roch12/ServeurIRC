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
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.Cursor;

public class ServerIRCWindow extends JFrame {

	private JPanel contentPane;
	private JTree tree;

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
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 10));
		contentPane.add(panel, BorderLayout.EAST);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "JPanel title", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_1);
		
		tree = new JTree();
		tree.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
		panel_1.add(tree);
		tree.setMaximumSize(new Dimension(200, 64));
		tree.setPreferredSize(new Dimension(173, 450));
	}

	public JTree getTree() {
		return tree;
	}
}
