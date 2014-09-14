package com.bsdarby.model;

import com.bsdarby.controller.VoterDataUI;
import com.bsdarby.view.PasswordDialog;
import javax.swing.*;


/**
 * Class in voterdata2/com.bsdarby.model.
 * Created by bsdarby on 9/14/14.
 */
public class VoterData {

	static DatabaseManager voterDB;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
					/* LOGIN */
				String[] userLogin = PasswordDialog.login();
					/* CONNECT to DataBase */
				voterDB = new DatabaseManager(userLogin[0], userLogin[1]);

				VoterDataUI controlPanel = new VoterDataUI(voterDB);
				controlPanel.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				controlPanel.setVisible(true);
			}
		});
	}

	public DatabaseManager getDB() {
		return voterDB;
	}

}
