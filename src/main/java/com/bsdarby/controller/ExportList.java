package com.bsdarby.controller;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.ResultSet;

/**
 * Class in voterdata2/com.bsdarby.controller.
 * Created by bsdarby on 9/30/14.
 */
public class ExportList extends JFrame{
	File exportFile;
	JButton btnSave, btnBrowse;
	JFileChooser fileChooser = new JFileChooser();
	JLabel lblFilePath;

	public ExportList(ResultSet resultSet){
		JPanel contentPane, pnlExpCentral, pnlExpSouth, pnlExpNorth;
		lblFilePath = new JLabel("No file selected");
		Desktop desktop = Desktop.getDesktop();
		JTextArea textArea = new JTextArea();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(new Dimension(400, 400));
		setLocationRelativeTo(null);
		contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		pnlExpCentral	= new JPanel(new FlowLayout());
		pnlExpNorth 	= new JPanel(new FlowLayout());
		pnlExpSouth 	= new JPanel(new FlowLayout());


		btnBrowse = new JButton("Choose File");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSaveDialog(fileChooser);
		}});
		contentPane.add(btnBrowse);

		btnSave = new JButton("Export");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveExportFile();
			}
		});
		btnSave.setEnabled(false);

		JLabel lblFile = new JLabel("File:");
		pnlExpNorth.add(lblFile);
		pnlExpNorth.add(lblFilePath);
		contentPane.add(pnlExpNorth, BorderLayout.NORTH);

		pnlExpSouth.add(btnBrowse);
		pnlExpSouth.add(btnSave);
		contentPane.add(pnlExpSouth, BorderLayout.SOUTH);


		validate();
		setVisible(true);


	}

	private void openSaveDialog(JFileChooser fileChooser){
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fileChooser.showSaveDialog(this);
		System.out.println("fileChooser: returnVal = "+ returnVal +", Selected: "+ fileChooser.getSelectedFile());
		if (JFileChooser.APPROVE_OPTION == returnVal) {
			exportFile = fileChooser.getSelectedFile();
			int pathLength = exportFile.getPath().length();
			String strLabelMsg;
			if(50 < pathLength) {
				strLabelMsg = "...";
				strLabelMsg += exportFile.getPath().substring(pathLength - 50);
			} else {
				strLabelMsg  = exportFile.getPath();
			}
			lblFilePath.setText(strLabelMsg);
			btnSave.setEnabled(true);
		}
	}

	private void saveExportFile(){

	}
}
