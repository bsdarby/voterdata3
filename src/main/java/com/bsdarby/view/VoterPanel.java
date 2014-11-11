package com.bsdarby.view;

import com.bsdarby.model.VoterTableModel;
import oracle.jrockit.jfr.openmbean.PresetFileType;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

/**
 * Class in voterdata2/com.bsdarby.view.
 * Created by bsdarby on 11/5/14.
 */
public class VoterPanel extends JPanel {
	JPanel dataPanelVoters;


	public VoterPanel( ResultSet voterDB ) {

		VoterTableModel vTblModel;
		JTable 					vTbl;
		JScrollPane 		voterPane;

		dataPanelVoters = new JPanel();
//		dataPanelVoters.setPreferredSize(new Dimension(width - cpWidth, vpHeight));
		dataPanelVoters.setMinimumSize(new Dimension(300, 100));


	}
}
