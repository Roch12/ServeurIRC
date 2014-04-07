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
	private JTextField txtPort;
	private JTextField txtAnonymous;
	private JTextField txtPassword;
	private JButton okButton;
	private JButton cancelButton;

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
			txtPort = new JTextField();
			txtPort.setText("4567");
			txtPort.setBounds(481, 13, 80, 32);
			contentPanel.add(txtPort);
			txtPort.setColumns(10);
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
		
		txtPassword = new JTextField();
		txtPassword.setBounds(193, 134, 368, 32);
		contentPanel.add(txtPassword);
		txtPassword.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
				}
			});
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Connexion");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
	}
	
	/**
	 * Récuperer le boutton de connexion
	 * @return JButton
	 */
	public JButton getOkButton() {
		return okButton;
	}
	
	/**
	 * Récuperer le JTextField de l'adresse IP
	 * @return
	 */
	public JTextField getTxtLocalhost() {
		return txtLocalhost;
	}
	
	/**
	 * Récupérer le JTextField du Port
	 * @return JTextField
	 */
	public JTextField getTxtPort() {
		return txtPort;
	}
	
	/**
	 * Récupérer le JTextField du pseudo
	 * @return JTextField
	 */
	public JTextField getTxtAnonymous() {
		return txtAnonymous;
	}
	
	/**
	 * Récupérer le JTextField du Password
	 * @return JTextField
	 */
	public JTextField getTxtPassword() {
		return txtPassword;
	}
	
	/**
	 * Récupérer le bouton de cancel
	 * @return JButton
	 */
	public JButton getCancelButton() {
		return cancelButton;
	}
}
