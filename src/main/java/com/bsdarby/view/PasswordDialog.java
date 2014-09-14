package com.bsdarby.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Class in voterdata2/com.bsdarby.view.
 * Created by bsdarby on 9/14/14.
 * This class provides a simple login dialog. The password is blanked when entering it.
 * The password is transmitted in cleartext with no provision for hashing or encryption.
 * <pre>
 * PRE:   True
 * POST:  Returns the username and password entered by the user.
 *</pre>
 * @author Brian Darby
 * @version 1.1
 * @author O'Reilly
 * @version 1.0
 *
 * TODO ... add facility for encrypting/hashing the password.
 */

public class PasswordDialog extends JDialog implements ActionListener, KeyListener {
	private final JTextField user;
	private final JPasswordField password;
	private static String[] info;
	private static boolean set = false;

	public PasswordDialog( final JFrame owner ) {
		//... set the dialog title and size
		super(owner, "Login", true);
		this.setSize(280, 150);
		this.setLocationRelativeTo(null);
		user = new JTextField(10);
		user.addActionListener(this);
		user.addKeyListener(this);
		password = new JPasswordField(10);
		password.addActionListener(this);
		password.addKeyListener(this);

		//...create the center panel which contains the fields for entering information
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(3, 2));
		center.add(new JLabel(" Enter UserName:"));
		center.add(user);
		center.add(new JLabel(" Enter Password:"));
		center.add(password);

		//...create the south panel which contains the buttons
		JPanel south = new JPanel();
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setActionCommand("SUBMIT");
		btnSubmit.addActionListener(this);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("CANCEL");
		btnCancel.addActionListener(this);

		JButton btnHelp = new JButton("Help");
		south.add(btnCancel);
		south.add(btnSubmit);

		/* ...add listeners to the buttons */
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent aEvent ) {
				JOptionPane.showMessageDialog(owner,
								"Your username and password were\ngiven you by your administrator.");
			}
		});
		//...add the panels to the dialog window
		Container contentPane = getContentPane();
		contentPane.add(center, BorderLayout.CENTER);
		contentPane.add(south, BorderLayout.SOUTH);
	}

	public void actionPerformed( ActionEvent e ) {
		String cmd = e.getActionCommand();
		if ("SUBMIT".equals(cmd))
		{
			String username = user.getText();
			if (username.length() == 0)
			{
				user.requestFocus();
			} else
			{
				char[] input = password.getPassword();
				if (input.length == 0)
				{
					password.requestFocus();
				} else
				{
					String passwd = new String(input);
					info = new String[2];
					if (username.length() > 0) { info[0] = username; }
					info[1] = passwd;
					set = true;
					dispose();
				}
			}
		} else if ("CANCEL".equals(cmd))
		{
			info = new String[2];
			info[0] = "CANCELED";
			info[1] = "CANCELED";
			set = true;
			dispose();
		}
	}

	public static void main( String[] args ) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		final PasswordDialog addPassword = new PasswordDialog(frame);
		addPassword.setVisible(true);
	}

	public static String[] login() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		final PasswordDialog addPassword = new PasswordDialog(frame);
		addPassword.setVisible(true);
		while (!set)
		{
			try
			{
				Thread.sleep(5000);
			} catch (InterruptedException e)
			{
				System.out.println("\nThere was an InterruptedException during login.");
				e.printStackTrace();
			}
		}
		return info;
	}

	@Override
	public void keyTyped( KeyEvent e ) {
		if (e.getSource().toString().contains("javax.swing.JPasswordField") ||
						e.getSource().toString().contains("javax.swing.JTextField"))
		{
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
			{
				String username = user.getText();
				if (username.length() == 0)
				{
					user.requestFocus();
				} else
				{
					char[] input = password.getPassword();
					if (input.length == 0)
					{
						password.requestFocus();
					} else
					{
						String passwd = new String(input);
						info = new String[2];
						if (username.length() > 0) { info[0] = username; }
						info[1] = passwd;
						set = true;
						dispose();
					}
				}
			}
		}
	}

	@Override
	public void keyPressed( KeyEvent e ) {

	}

	@Override
	public void keyReleased( KeyEvent e ) {

	}
}
