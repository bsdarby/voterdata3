package com.bsdarby.view;

import com.bsdarby.model.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Class in voterdata2/com.bsdarby.view.
 * Created by bsdarby on 11/5/14.
 */
public class VoterMainView extends JFrame {
	private DatabaseManager voterDB;
	private NumberFormat df1 = new DecimalFormat("#,###0");
	private ResultSet resultSet;
	JPanel dataPanel;
	JPanel dataPanelVoters;
	JPanel dataPanelHistory;


	public VoterMainView() {

		// open database

		// new content frame
		// add new ControlPanel
		// add new VoterPanel
		// add new HistoryPanel
		// add new VoterSearch
		// add new HistorySearch

		Container vdPane = getContentPane();
		ControlPanel controlPanel = new ControlPanel();
		VoterPanel voterPanel = new VoterPanel(resultSet);
		HistoryPanel historyPanel = new HistoryPanel();



	}

}
