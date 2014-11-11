package com.bsdarby.controller;

import com.bsdarby.model.TextArranger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

/**
 * Class in voterdata2/com.bsdarby.controller.
 * Created by bsdarby on 9/30/14.
 */
public class DataExporter extends JFrame{

	JButton chooseFile, doExport;
	File targetFile;
	JFileChooser fileChooser = new JFileChooser();
	JLabel lblShowFilePath, lblFile;


	public DataExporter( ResultSet resultSet ) {
		lblShowFilePath = new JLabel("No file selected");

		chooseFile = new JButton("Choose File");
		chooseFile.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				openSaveDialog(fileChooser);
			}
		});

		doExport = new JButton("Export");
		doExport.setEnabled(false);
		doExport.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				saveExportFile();
			}
		});

		buildDisplayPanel(getContentPane());

	}

	private void buildDisplayPanel( Container contentPane ) {

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(new Dimension(600, 400));
		setLocationRelativeTo(null);
		contentPane.setLayout(new BorderLayout());

		JPanel pnlNorth		= new JPanel(new FlowLayout());
		JPanel pnlCentral	= new JPanel(new FlowLayout());
		JPanel pnlSouth		= new JPanel(new FlowLayout());

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed( ActionEvent e){
				dispose();
			}
		});

		lblFile = new JLabel("File: No file selected.");
		pnlNorth.add(lblFile);
//		pnlNorth.add(lblShowFilePath);
		contentPane.add(pnlNorth, BorderLayout.NORTH);

		pnlSouth.add(chooseFile);
		pnlSouth.add(doExport);
		pnlSouth.add(cancel);
		contentPane.add(pnlSouth, BorderLayout.SOUTH);

		validate();
		setVisible(true);
	}

	private void openSaveDialog(JFileChooser fileChooser){
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fileChooser.showSaveDialog(this);
		System.out.println("fileChooser: returnVal = "+ returnVal
						+ ", compare to " + JFileChooser.APPROVE_OPTION
						+", Selected: " + fileChooser.getSelectedFile());
		if (JFileChooser.APPROVE_OPTION == returnVal) {
			targetFile = fileChooser.getSelectedFile();
			int maxLabelLength = 70;
//			lblShowFilePath.setText(TextArranger.makeEllipsis(targetFile.getPath(),maxLabelLength));
//			System.out.println("Text Arranger results: "
//							+ TextArranger.makeEllipsis(targetFile.getPath(), maxLabelLength));
			lblFile.setText("File: " + TextArranger.makeEllipsis(targetFile.getPath(),maxLabelLength));
			System.out.println("Length of filename = " +
							(TextArranger.makeEllipsis(targetFile.getPath(), maxLabelLength).length() + 6)
							+ ", ");
			doExport.setEnabled(true);
		}
	}

	private void saveExportFile(){
		try {
			PrintWriter output = new PrintWriter(targetFile);
		} catch (IOException e) {
			System.err.println("IO Exception caught at saveExportFile.");
			e.printStackTrace();
		}


	}

	/**
	 *
	 "SELECT
	 voters.lVoterUniqueID,
	 voters.szNameLast,
	 voters.szNameFirst,
	 voters.sGender,
	 voters.szPhone,
	 voters.szSitusAddress,
	 voters.szSitusCity,
	 voters.sSitusZip,
	 voters.sPrecinctID,
	 voters.szPartyName,
	 dtBirthDate,
	 dtOrigRegDate,
	 DATEDIFF(CURDATE(), dtBirthDate) AS age,
	 DATEDIFF(CURDATE(), dtOrigRegDate) AS agoreg
	 FROM voters
	 JOIN
	 history ON history.lVoterUniqueID = voters.lVoterUniqueID
	 WHERE
	 voters.sPrecinctID LIKE 4322
	 GROUP BY voters.szSitusAddress
	 HAVING count(IF(history.szCountedFlag = 'YES',1,NULL)) >= 2
	 AND age / 365 >= 17
	 AND ((age) / 365) - 1 <= 130
	 AND agoreg / 365 >= 0
	 ORDER BY CAST(sPrecinctID as unsigned) , szStreetName , sStreetSuffix ,
	 CAST(sHouseNum as unsigned) , CAST(sUnitNum as unsigned) , szNameLast ,
	 szNameFirst , voters.lVoterUniqueID"

	 */
}
