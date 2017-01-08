package main;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JButton;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;

import types.Contact;
import misc.Constants;
import network.Client;
import network.Server;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.SwingConstants;

public class Login {

	private JFrame frmShh;
	private JTextField usernameTextField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if(args.length!=0 && args[0].equalsIgnoreCase("SERVER")){
			Server server = new Server();
			try {
				server.mainServerLoop();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{ //CLIENT
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Login window = new Login();
						window.frmShh.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});			
		}
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		frmShh = new JFrame();
		frmShh.setTitle("Shh...");
		frmShh.setResizable(false);
		frmShh.getContentPane().setBackground(Color.WHITE);
		frmShh.setBounds(100, 100, 541, 461);
		frmShh.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{89, 380, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 34, 52, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frmShh.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Login.class.getResource("/pictures/WBPb_pA-.jpg")));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 4;
		gbc_lblNewLabel.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		frmShh.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblUsername = new JLabel("Nickname:");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.gridheight = 2;
		gbc_lblUsername.anchor = GridBagConstraints.SOUTHEAST;
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 1;
		frmShh.getContentPane().add(lblUsername, gbc_lblUsername);
		
		usernameTextField = new JTextField();
		lblUsername.setLabelFor(usernameTextField);
		GridBagConstraints gbc_usernameTextField = new GridBagConstraints();
		gbc_usernameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_usernameTextField.anchor = GridBagConstraints.SOUTH;
		gbc_usernameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_usernameTextField.gridx = 1;
		gbc_usernameTextField.gridy = 2;
		frmShh.getContentPane().add(usernameTextField, gbc_usernameTextField);
		usernameTextField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.SOUTHEAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 3;
		frmShh.getContentPane().add(lblPassword, gbc_lblPassword);
		
		passwordField = new JPasswordField();
		lblPassword.setLabelFor(passwordField);
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.anchor = GridBagConstraints.SOUTH;
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.gridx = 1;
		gbc_passwordField.gridy = 3;
		frmShh.getContentPane().add(passwordField, gbc_passwordField);
		
		JButton btnNewButton = new JButton("Shh...");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(usernameTextField.getText().isEmpty() || passwordField.getPassword().length == 0){
					JOptionPane.showMessageDialog(null, Constants.ERROR_BAD_USERPASS);
				}else{
					//verify username & password
					if(Chat.getClient().autenticate(usernameTextField.getText(), passwordField.getPassword())){
						//set cuurent owner
						Chat.setCurrentUser(Chat.getClient().getContact(usernameTextField.getText()));
						//open chat form
						frmShh.dispose();//setVisible(false);
						new Chat().setVisible(true);
					}else{
						JOptionPane.showMessageDialog(null, Constants.ERROR_AUTHENTICATION);
					}
				}
			}
		});
		
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.VERTICAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 4;
		frmShh.getContentPane().add(btnNewButton, gbc_btnNewButton);
		
		

	}

}
