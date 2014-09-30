package com.bsdarby.controller;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Class in voterdata2/com.bsdarby.controller.
 * Created by bsdarby on 9/30/14.
 */
public class ExportList extends JFrame{

	private JPanel contentPane;
	private JFileChooser fileChooser = new JFileChooser();
	private JButton btnSave, btnBrowse;
	private JLabel lblFolderName = new JLabel("No folder selected.");
	private Desktop desktop = Desktop.getDesktop();
	private JTextArea textArea = new JTextArea();

	public ExportList(ResultSet resultSet){
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(new Dimension(400, 400));
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());

		btnBrowse = new JButton("Choose Save Location");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSaveDialog(fileChooser, lblFolderName, true);
		}});
		contentPane.add(btnBrowse);

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveExportFile();
			}
		});

		JLabel lblFolder = new JLabel("Folder:");
		JPanel pnlExportNorth = new JPanel(new GridLayout(0,2));
		pnlExportNorth.add(lblFolder);
		pnlExportNorth.add(lblFolderName);
		contentPane.add(pnlExportNorth);
		contentPane.add(btnSave, BorderLayout.SOUTH);
		validate();
		setVisible(true);


	}

	private void openSaveDialog(JFileChooser fileChooser, JLabel lblFolderName, boolean directoryOnly){
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	}

	private void saveExportFile(){

	}
}
