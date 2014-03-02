package com.mroch.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateDialogConnection extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtLocalhost;
	private JTextField textField_1;
	private JTextField txtAnonymous;
	private JTextField textField_3;
	private JButton okButton;

	/**
	 * Create the dialog.
	 */
	public CreateDialogConnection() {
		setResizable(false);
		setBounds(100, 100, 644, 316);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblAdresse = new JLabel("Adresse : ");
			lblAdresse.setBounds(60, 13, 94, 26);
			contentPanel.add(lblAdresse);
		}
		{
			txtLocalhost = new JTextField();
			txtLocalhost.setText("localhost");
			txtLocalhost.setBounds(159, 10, 186, 32);
			contentPanel.add(txtLocalhost);
			txtLocalhost.setColumns(10);
		}
		{
			JLabel lblPort = new JLabel("Port : ");
			lblPort.setBounds(414, 16, 59, 26);
			contentPanel.add(lblPort);
		}
		{
			textField_1 = new JTextField();
			textField_1.setText("4567");
			textField_1.setBounds(481, 13, 80, 32);
			contentPanel.add(textField_1);
			textField_1.setColumns(10);
		}
		
		JLabel lblUsername = new JLabel("Username : ");
		lblUsername.setBounds(60, 83, 129, 26);
		contentPanel.add(lblUsername);
		
		txtAnonymous = new JTextField();
		txtAnonymous.setText("Anonymous");
		txtAnonymous.setBounds(193, 80, 368, 32);
		contentPanel.add(txtAnonymous);
		txtAnonymous.setColumns(10);
		
		JLabel lblMotDePasse = new JLabel("Mot de passe :");
		lblMotDePasse.setBounds(29, 137, 160, 26);
		contentPanel.add(lblMotDePasse);
		
		textField_3 = new JTextField();
		textField_3.setBounds(193, 134, 368, 32);
		contentPanel.add(textField_3);
		textField_3.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Connexion");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
	}
	public JButton getOkButton() {
		return okButton;
	}
	public JTextField getTxtLocalhost() {
		return txtLocalhost;
	}
	public JTextField getTextField_1() {
		return textField_1;
	}
	public JTextField getTxtAnonymous() {
		return txtAnonymous;
	}
	public JTextField getTextField_3() {
		return textField_3;
	}
}
