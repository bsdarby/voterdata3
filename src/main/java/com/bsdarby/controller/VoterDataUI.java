package com.bsdarby.controller;

import com.bsdarby.model.Database;
import com.bsdarby.model.HistoryTableModel;
import com.bsdarby.model.VoterTableModel;
import com.bsdarby.view.VoterMainView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PageFormat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Class in VoterData/controller.
 * Created by bsdarby on 8/27/14.
 */
public class VoterDataUI extends JFrame implements KeyListener, RowSorterListener {
	private Database voterDB;
	private NumberFormat df1 = new DecimalFormat("#,###0");

	private static final Double WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private static final Double HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public static int width = WIDTH.intValue() - 5;
	public static int height = HEIGHT.intValue() - 5;
	int cpWidth = 200;
	int cpHeight = height;
	int vpHeight = height * 4 / 5;
	int hpHeight = height - vpHeight;
	String tooltipText;
	ResultSet resultSet = null;
	ResultSet resultSetH = null;
	ResultSet resultSetW = null;
	Integer voterID = 0;
	int voterIDTrigger = 0;
	private PageFormat pageFormat;
	private Graphics graphics;
	private int pages;
	String precinct;
	String ageMin;
	String ageMax;
	Integer regago;
	Integer numvotes;

	protected int row = 0; /* this put here to prevent double
	processing of history search upon clicking in a voter history row */

	Container vdPane;
	JPanel dataPanel;
	JPanel dataPanelVoters;
	JPanel dataPanelHistory;

	/* Tables */
	private VoterTableModel vTblModel;
	private JTable vTbl;
	private JScrollPane voterPane;
	private HistoryTableModel hTblModel;
	private JTable hTbl;
	private JScrollPane historyPane;

	public final static Font sansHeading = new Font("Lato Black", Font.BOLD, 15);
	public final static Font sansLabel = new Font("Lato Medium", Font.PLAIN, 13);
	public final static Font sansTable = new Font("Lato Medium", Font.PLAIN, 13);
	public final static Font sansField = new Font("Lato Bold", Font.BOLD, 14);

	/* Search Fields / Labels / Buttons */
	JTextField tfFirstName,
					tfLastName,
					tfPrecinct,
					tfZip,
					tfElectionExclude,
					tfLat,
					tfLong,
					tfStreet,
					tfStreetNo,
					tfCity,
					tfParty,
					tfNumVotes,
					tfAgeMin,
					tfAgeMax,
					tfRegAgo;
	JButton btnSearch,
					btnHistory,
					btnPrint,
					btnClear,
					btnExport,
					btnHelp,
					btnExit;
	JLabel lblSpacer,
					lblLast,
					lblFirst,
					lblPrecinct,
					lblZip,
					lblElectionExclude,
					lblLat,
					lblLong,
					lblStreet,
					lblStreetNo,
					lblCity,
					lblParty,
					lblMailVoter,
					lblPollVoter,
					lblAgeMin,
					lblAgeMax,
					lblNumVotes,
					lblRegAgo,
					lblCtlPanel,
					lblVoterPanel,
					lblHistoryPanel;
	JCheckBox cbEven,
					cbOdd,
					cbMailVoter,
					cbPollVoter;

	public VoterDataUI( final Database voterDB ) {
		this.voterDB = voterDB;
		setSize(new Dimension(width, height));
		setLocationRelativeTo(null);
		setTitle("Voter Data");
		vdPane = getContentPane();
		vdPane.setLayout(new BorderLayout());

		/**
		 * Build Main View
		 */

		VoterMainView mainPanel = new VoterMainView(getContentPane());



		/* Panels */
		JPanel northPanel;
		JPanel ctlPanel;
		JPanel ctlPanelCenter;
		JPanel ctlPanelCenterNorth;
		JPanel ctlPanelCenterCenter;
		JPanel ctlPanelSouth;

		northPanel = new JPanel();
		JMenuBar menuBar = new JMenuBar();
		northPanel.add(menuBar);
		northPanel.validate();

		ctlPanel = new JPanel(new BorderLayout());
		ctlPanel.setPreferredSize(new Dimension(cpWidth, cpHeight));
		ctlPanelCenter = new JPanel(new BorderLayout(0, 10));
		ctlPanelCenter.setPreferredSize(new Dimension(cpWidth, cpHeight - 300));
		ctlPanelSouth = new JPanel(new GridLayout(0, 2, 10, 10));
		ctlPanelCenterCenter = new JPanel(new GridLayout(0, 2, 3, 10));
		ctlPanelCenterCenter.addKeyListener(this);
		ctlPanelCenterCenter.setFocusTraversalKeysEnabled(true);
		ctlPanelCenterNorth = new JPanel(new GridLayout(1, 0, 10, 10));

		ctlPanel.add(ctlPanelCenter, BorderLayout.CENTER);
		ctlPanel.add(ctlPanelSouth, BorderLayout.SOUTH);
		ctlPanel.validate();

		dataPanel = new JPanel(new BorderLayout());
		dataPanel.setPreferredSize(new Dimension(width - cpWidth, height));
		dataPanel.setMinimumSize(new Dimension(300, 150));

		dataPanelVoters = new JPanel();
		dataPanelVoters.setPreferredSize(new Dimension(width - cpWidth, vpHeight));
		dataPanelVoters.setMinimumSize(new Dimension(300, 100));

		dataPanelHistory = new JPanel();
		dataPanelHistory.setPreferredSize(new Dimension(width - cpWidth, hpHeight));
		dataPanelHistory.setMinimumSize(new Dimension(300, 50));


		//noinspection UnusedAssignment
			/* Labels */
		lblMailVoter = new JLabel("Mail Voters");
		lblMailVoter.setHorizontalAlignment(JLabel.RIGHT);
		lblMailVoter.setForeground(Color.blue.darker().darker());
		lblMailVoter.setFont(sansLabel);

		lblPollVoter = new JLabel("Poll Voters");
		lblPollVoter.setHorizontalAlignment(JLabel.RIGHT);
		lblPollVoter.setForeground(Color.blue.darker().darker());
		lblPollVoter.setFont(sansLabel);

		lblPrecinct = new JLabel("Precinct");
		lblPrecinct.setHorizontalAlignment(JLabel.RIGHT);
		lblPrecinct.setForeground(Color.blue.darker().darker());
		lblPrecinct.setFont(sansLabel);

		lblStreetNo = new JLabel("Street No");
		lblStreetNo.setHorizontalAlignment(JLabel.RIGHT);
		lblStreetNo.setForeground(Color.blue.darker().darker());
		lblStreetNo.setFont(sansLabel);

		lblStreet = new JLabel("Street");
		lblStreet.setHorizontalAlignment(JLabel.RIGHT);
		lblStreet.setForeground(Color.blue.darker().darker());
		lblStreet.setFont(sansLabel);

		lblParty = new JLabel("Party");
		lblParty.setHorizontalAlignment(JLabel.RIGHT);
		lblParty.setForeground(Color.blue.darker().darker());
		lblParty.setFont(sansLabel);

		lblLast = new JLabel("Last Name");
		lblLast.setHorizontalAlignment(JLabel.RIGHT);
		lblLast.setForeground(Color.blue.darker().darker());
		lblLast.setFont(sansLabel);

		lblFirst = new JLabel("First Name");
		lblFirst.setHorizontalAlignment(JLabel.RIGHT);
		lblFirst.setForeground(Color.blue.darker().darker());
		lblFirst.setFont(sansLabel);

		lblCity = new JLabel("City");
		lblCity.setHorizontalAlignment(JLabel.RIGHT);
		lblCity.setForeground(Color.blue.darker().darker());
		lblCity.setFont(sansLabel);

		lblZip = new JLabel("Zip");
		lblZip.setHorizontalAlignment(JLabel.RIGHT);
		lblZip.setForeground(Color.blue.darker().darker());
		lblZip.setFont(sansLabel);

		lblElectionExclude = new JLabel("Exc. Election");
		lblElectionExclude.setHorizontalAlignment(JLabel.RIGHT);
		lblElectionExclude.setForeground(Color.green.darker().darker());
		lblElectionExclude.setFont(sansLabel);

		lblNumVotes = new JLabel("Times Voted");
		lblNumVotes.setHorizontalAlignment(JLabel.RIGHT);
		lblNumVotes.setForeground(Color.green.darker().darker());
		lblNumVotes.setFont(sansLabel);

		lblRegAgo = new JLabel("Reg How Long");
		lblRegAgo.setHorizontalAlignment(JLabel.RIGHT);
		lblRegAgo.setForeground(Color.green.darker().darker());
		lblRegAgo.setFont(sansLabel);

		lblAgeMin = new JLabel("Min Age");
		lblAgeMin.setHorizontalAlignment(JLabel.RIGHT);
		lblAgeMin.setForeground(Color.green.darker().darker());
		lblAgeMin.setFont(sansLabel);

		lblAgeMax = new JLabel("Max Age");
		lblAgeMax.setHorizontalAlignment(JLabel.RIGHT);
		lblAgeMax.setForeground(Color.green.darker().darker());
		lblAgeMax.setFont(sansLabel);
/*
		lblLat = new JLabel("Latitude");
		lblLat.setHorizontalAlignment(JLabel.RIGHT);
		lblLat.setForeground(Color.gray);
		lblLat.setFont(sansLabel);

		lblLong = new JLabel("Longitude");
		lblLong.setHorizontalAlignment(JLabel.RIGHT);
		lblLong.setForeground(Color.gray);
		lblLong.setFont(sansLabel);
*/
		lblCtlPanel = new JLabel("Control Panel");
		lblCtlPanel.setHorizontalAlignment(JLabel.CENTER);
		lblCtlPanel.setFont(sansHeading);
		lblCtlPanel.setForeground(Color.green.darker().darker());

		tooltipText = "This is the number of voters selected by your current search parameters.";
		lblVoterPanel = new JLabel("Voters");
		lblVoterPanel.setHorizontalAlignment(JLabel.CENTER);
		lblVoterPanel.setFont(sansHeading);
		lblVoterPanel.setToolTipText(tooltipText);
		lblVoterPanel.setForeground(Color.green.darker().darker());

		tooltipText = "This is the voting history of the currently selected voter above.";
		lblHistoryPanel = new JLabel("History");
		lblHistoryPanel.setHorizontalAlignment(JLabel.CENTER);
		lblHistoryPanel.setFont(sansHeading);
		lblHistoryPanel.setToolTipText(tooltipText);
		lblHistoryPanel.setForeground(Color.green.darker().darker());

			/* Text Fields */
		tfPrecinct = new JTextField(4);
		tfPrecinct.setFont(sansField);
		tfPrecinct.setForeground(Color.magenta.darker());
		tfPrecinct.addKeyListener(this);

		tfStreetNo = new JTextField(3);
		tfStreetNo.setFont(sansField);
		tfStreetNo.setForeground(Color.magenta.darker());
		tfStreetNo.addKeyListener(this);

		tfStreet = new JTextField(10);
		tfStreet.setFont(sansField);
		tfStreet.setForeground(Color.magenta.darker());
		tfStreet.addKeyListener(this);

		tfParty = new JTextField(15);
		tfParty.setFont(sansField);
		tfParty.setForeground(Color.magenta.darker());
		tfParty.addKeyListener(this);

		tfLastName = new JTextField(15);
		tfLastName.setFont(sansField);
		tfLastName.setForeground(Color.magenta.darker());
		tfLastName.addKeyListener(this);

		tfFirstName = new JTextField(15);
		tfFirstName.setFont(sansField);
		tfFirstName.setForeground(Color.magenta.darker());
		tfFirstName.addKeyListener(this);

		tfCity = new JTextField(15);
		tfCity.setFont(sansField);
		tfCity.setForeground(Color.magenta.darker());
		tfCity.addKeyListener(this);

		tfZip = new JTextField(4);
		tfZip.setFont(sansField);
		tfZip.setForeground(Color.magenta.darker());
		tfZip.addKeyListener(this);

		tfElectionExclude = new JTextField(8);
		tfElectionExclude.setFont(sansField);
		tfElectionExclude.setForeground(Color.magenta);
		tfElectionExclude.addKeyListener(this);

		tfNumVotes = new JTextField(1);
		tfNumVotes.setFont(sansField);
		tfNumVotes.setForeground(Color.magenta);
		tfNumVotes.setText("2");
		tfNumVotes.addKeyListener(this);

		tfRegAgo = new JTextField(2);
		tfRegAgo.setFont(sansField);
		tfRegAgo.setText("0");
		tfRegAgo.setForeground(Color.magenta);
		tfRegAgo.addKeyListener(this);
//		tfRegAgo.setText("Coming Soon");
//		tfRegAgo.setEnabled(false);

		tfAgeMin = new JTextField(2);
		tfAgeMin.setFont(sansField);
		tfAgeMin.setText("17");
		tfAgeMin.setForeground(Color.magenta);
		tfAgeMin.addKeyListener(this);
//		tfAgeMin.setText("Coming Soon");
//		tfAgeMin.setEnabled(false);

		tfAgeMax = new JTextField(2);
		tfAgeMax.setFont(sansField);
		tfAgeMax.setText("130");
		tfAgeMax.setForeground(Color.magenta);
		tfAgeMax.addKeyListener(this);

		tfLat = new JTextField(8);
		tfLat.setText("Coming Soon");
		tfLat.setEnabled(false);
//		tfLat.setFont(sansField);
		tfLat.setForeground(Color.magenta);
//		tfLat.addKeyListener(this);

		tfLong = new JTextField(8);
		tfLong.setText("Coming Soon");
		tfLong.setEnabled(false);
//		tfLong.setFont(sansField);
		tfLong.setForeground(Color.magenta);
//		tfLong.addKeyListener(this);

			/* checkboxes */
		tooltipText = "Show even numbered addresses only.";
		cbEven = new JCheckBox("Even #'s");
		cbEven.setHorizontalTextPosition(SwingConstants.LEFT);
		cbEven.setFont(sansLabel);
		cbEven.setToolTipText(tooltipText);
		cbEven.setForeground(Color.green.darker().darker());
		cbEven.setSelected(true);

		tooltipText = "Show odd numbered addresses only.";
		cbOdd = new JCheckBox("Odd  #'s");
		cbOdd.setHorizontalTextPosition(SwingConstants.LEFT);
		cbOdd.setFont(sansLabel);
		cbOdd.setToolTipText(tooltipText);
		cbOdd.setForeground(Color.green.darker().darker());
		cbOdd.setSelected(true);

		tooltipText = "Include vote by mail voters.";
		cbMailVoter= new JCheckBox("By Mail");
		cbMailVoter.setHorizontalTextPosition(SwingConstants.LEFT);
		cbMailVoter.setFont(sansLabel);
		cbMailVoter.setToolTipText(tooltipText);
		cbMailVoter.setForeground(Color.green.darker().darker());
		cbMailVoter.setSelected(true);

		tooltipText = "Include voters who vote at the polls.";
		cbPollVoter = new JCheckBox("At Polls");
		cbPollVoter.setHorizontalTextPosition(SwingConstants.LEFT);
		cbPollVoter.setFont(sansLabel);
		cbPollVoter.setToolTipText(tooltipText);
		cbPollVoter.setForeground(Color.green.darker().darker());
		cbPollVoter.setSelected(true);



			/* Buttons */
		tooltipText = "To search for voters, one of the blue fields must have an entry.";
		btnSearch = new JButton("Search");
		btnSearch.setForeground(Color.blue.darker().darker());
		btnSearch.setFont(sansLabel);
		btnSearch.setToolTipText(tooltipText);

		btnPrint = new JButton("Print");
		btnPrint.setForeground(Color.blue.darker().darker());
		btnPrint.setFont(sansLabel);

		btnClear = new JButton("Clear");
		btnClear.setForeground(Color.green.darker().darker());
		btnClear.setFont(sansLabel);

		btnExport = new JButton("Export");
		btnExport.setForeground(Color.blue.darker().darker());
		btnExport.setFont(sansLabel);

		btnHelp = new JButton("Help");
		btnHelp.setForeground(Color.green.darker().darker());
		btnHelp.setFont(sansLabel);

		btnExit = new JButton("Exit");
		btnExit.setForeground(Color.red.darker().darker());
		btnExit.setFont(sansLabel);

			/* Build Search Panel */
		tooltipText = "To search for voters, one of the blue fields must have an entry.";
		ctlPanelCenterCenter.setToolTipText(tooltipText);
//		ctlPanelCenterCenter.add(lblMailVoter);
		ctlPanelCenterCenter.add(cbMailVoter);
//		ctlPanelCenterCenter.add(lblPollVoter);
		ctlPanelCenterCenter.add(cbPollVoter);
		ctlPanelCenterCenter.add(lblPrecinct);
		ctlPanelCenterCenter.add(tfPrecinct);
		ctlPanelCenterCenter.add(lblStreetNo);
		ctlPanelCenterCenter.add(tfStreetNo);
		ctlPanelCenterCenter.add(lblStreet);
		ctlPanelCenterCenter.add(tfStreet);
		ctlPanelCenterCenter.add(lblParty);
		ctlPanelCenterCenter.add(tfParty);
		ctlPanelCenterCenter.add(lblLast);
		ctlPanelCenterCenter.add(tfLastName);
		ctlPanelCenterCenter.add(lblFirst);
		ctlPanelCenterCenter.add(tfFirstName);
		ctlPanelCenterCenter.add(lblCity);
		ctlPanelCenterCenter.add(tfCity);
		ctlPanelCenterCenter.add(lblZip);
		ctlPanelCenterCenter.add(tfZip);
		ctlPanelCenterCenter.add(lblElectionExclude);
		ctlPanelCenterCenter.add(tfElectionExclude);
		ctlPanelCenterCenter.add(lblNumVotes);
		ctlPanelCenterCenter.add(tfNumVotes);
		ctlPanelCenterCenter.add(lblRegAgo);
		ctlPanelCenterCenter.add(tfRegAgo);
		ctlPanelCenterCenter.add(lblAgeMin);
		ctlPanelCenterCenter.add(tfAgeMin);
		ctlPanelCenterCenter.add(lblAgeMax);
		ctlPanelCenterCenter.add(tfAgeMax);
	/*
		ctlPanelCenterCenter.add(lblLat);
		ctlPanelCenterCenter.add(tfLat);
		ctlPanelCenterCenter.add(lblLong);
		ctlPanelCenterCenter.add(tfLong);
	*/
		ctlPanelCenterCenter.validate();

		tooltipText = "If both, or neither, checkbox is selected, you will get all addresses.";
		ctlPanelCenterNorth.setToolTipText(tooltipText);
		ctlPanelCenterNorth.add(cbEven);
		ctlPanelCenterNorth.add(cbOdd);
		ctlPanelCenterNorth.validate();

		ctlPanelCenter.add(ctlPanelCenterCenter, BorderLayout.CENTER);
		ctlPanelCenter.add(ctlPanelCenterNorth, BorderLayout.NORTH);
		ctlPanelCenter.validate();

		ctlPanelSouth.add(btnSearch);
		ctlPanelSouth.add(btnPrint);
		ctlPanelSouth.add(btnClear);
		ctlPanelSouth.add(btnExport);
		ctlPanelSouth.add(btnHelp);
		ctlPanelSouth.add(btnExit);
		ctlPanelSouth.validate();

		ctlPanel.add(lblCtlPanel, BorderLayout.NORTH);
		ctlPanel.add(ctlPanelCenter, BorderLayout.CENTER);
		ctlPanel.add(ctlPanelSouth, BorderLayout.SOUTH);
		ctlPanel.validate();

		dataPanelVoters.add(lblVoterPanel, BorderLayout.NORTH);
		dataPanelHistory.add(lblHistoryPanel, BorderLayout.NORTH);

		dataPanel.add(dataPanelVoters, BorderLayout.CENTER);
		dataPanel.add(dataPanelHistory, BorderLayout.SOUTH);
		dataPanel.validate();

		vdPane = getContentPane();
		vdPane.add(ctlPanel, BorderLayout.WEST);
		vdPane.add(dataPanel, BorderLayout.CENTER);
		vdPane.add(northPanel, BorderLayout.NORTH);
		vdPane.validate();
		vdPane.setVisible(true);

			/* ActionListeners for Fields */
		class FieldListener implements ActionListener {
			public void actionPerformed( ActionEvent evt ) {

			}
		}

/*			// ActionListeners for Buttons
		btnHistory.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent evt ) {
//				historySearch(voterDB);
			}
		});
*/

		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent evt ) {
				PrintWalkList printWalkList;
				printWalkList = new PrintWalkList(resultSetW, precinct);

/*				try {
					boolean	complete = vTbl.print();
				} catch (PrinterException e) {
					System.out.println("A Printer Exception was caught at the Action Listener for btnPrint.");
					e.printStackTrace();
				}
*/
//				PrintControl printControl = new PrintControl();
			}
		});

		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DataExporter(resultSet);
		}});

		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent evt ) {
				help();
		}});

		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent evt ) {
				voterDB.closeDBResources();
				System.exit(0);
		}});

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				getVoters();
			}
		});

		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				cbEven.setSelected(true);
				cbOdd.setSelected(true);
				cbMailVoter.setSelected(true);
				cbPollVoter.setSelected(true);
				tfFirstName.setText("");
				tfLastName.setText("");
				tfPrecinct.setText("");
				tfZip.setText("");
				tfElectionExclude.setText("");
//				tfLat.setText("");
//				tfLong.setText("");
				tfStreet.setText("");
				tfStreetNo.setText("");
				tfCity.setText("");
				tfParty.setText("");
				tfNumVotes.setText("2");
				tfAgeMax.setText("130");
				tfRegAgo.setText("0");
				tfPrecinct.requestFocus();
			}
		});
		tfPrecinct.requestFocus();

	}

	private void historySearch(Integer voterID, Database voterDB) {
		ResultSet resultSetH;
		String whereClauseH = " WHERE " +
						"lVoterUniqueId " +
						"LIKE '" + voterID + "' ";
		String selectH = " SELECT " +
						"szCountedFlag, " +
						"dtElectionDate, " +
						"szElectionDesc, " +
						"sElecTypeDesc, " +
						"sVotingPrecinct, " +
						"szVotingMethod, " +
						"szPartyName " +
						"FROM history ";
		String orderByH = " ORDER BY " +
						"szCountedFlag DESC, " +
						"dtElectionDate DESC ";
		String queryH = selectH + whereClauseH + orderByH;
//		System.out.println("queryH = " + queryH);
		resultSetH = doQueryH(queryH, voterDB);
//		return resultSetH;

		if (null != historyPane) {
			dataPanel.remove(lblHistoryPanel);
			dataPanelHistory.remove(historyPane);
		}

		HistoryTableModel hTblModel = new HistoryTableModel(resultSetH);
		hTbl = new JTable(hTblModel);
		hTbl.setRowSorter(new TableRowSorter<TableModel>(hTblModel));
		hTbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		hTbl.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		hTbl.setFillsViewportHeight(true);
		hTbl.setFont(sansTable);
		historyPane = new JScrollPane();
		historyPane.setPreferredSize(new Dimension(width - cpWidth, 150));
		historyPane.setViewportView(hTbl);

		dataPanelHistory.add(historyPane);
		dataPanelHistory.validate();
		dataPanel.validate();
		vdPane.validate();
		setVisible(true);
	}


	private ResultSet voterSearch(Database voterDB) {

		boolean boolShowTrans = false;
		boolean boolNumVotes = false;
		String whereClause;
		String selectNumVotes;
		String having;
		String groupBy;
		String orderBy;
		String query;

		String last = SafeChar.text1(tfLastName.getText());
		String first = SafeChar.text1(tfFirstName.getText());
		String party = SafeChar.text1(tfParty.getText());
		String city = SafeChar.text1(tfCity.getText());
		precinct = SafeChar.text1(tfPrecinct.getText());
		String zip = SafeChar.text1(tfZip.getText());
		String excludeIfVotedInElection = SafeChar.text1(tfElectionExclude.getText());
		String streetno = SafeChar.text1(tfStreetNo.getText());
		String street = SafeChar.text1(tfStreet.getText());
		String ageMin = SafeChar.num2(tfAgeMin.getText());
		String ageMax = SafeChar.num2(tfAgeMax.getText());


		boolean oddOnly  = false;
		boolean evenOnly = false;

		if ((cbEven.isSelected() && cbOdd.isSelected())
				|| (!cbEven.isSelected() && !cbOdd.isSelected()))
		{
			oddOnly = false;
			evenOnly = false;
		} else if (cbEven.isSelected())
		{
			evenOnly = true;
		} else if (cbOdd.isSelected())
		{
			oddOnly = true;
		}
//		System.out.println("cbEven = "+ cbEven.isSelected() + ", cdOdd = "+ cbOdd.isSelected());

		boolean mailVoters = false;
		boolean pollVoters = false;

		if((cbMailVoter.isSelected() && cbPollVoter.isSelected())
			|| (!cbMailVoter.isSelected() && !cbPollVoter.isSelected() )) {
			mailVoters = true;
			pollVoters = true;
		} else if(cbMailVoter.isSelected()) {
			mailVoters = true;
		} else if (cbPollVoter.isSelected()) {
			pollVoters = true;
		}


		/**
		 * Convert strings to Integers
		 */
			String temp;
		temp = SafeChar.num2(tfNumVotes.getText());
		if (temp.length() > 0) {
			numvotes = Integer.parseInt(temp);
		} else
		{
			numvotes = 0;
		}

		temp = SafeChar.num2(tfRegAgo.getText());
		if (temp.length() > 0)
		{
			regago = Integer.parseInt(temp);
		} else
		{
			regago = 0;
		}


/*		String latitude		= SafeChar.text1(tfLat.getText());
		latitude					=	latitude.replace("\'", "\'\'");
		latitude					=	latitude.replaceAll("[*]+", "%");
		latitude					=	latitude.replaceAll("[?]", "_");

		String longitude	= SafeChar.text1(tfLong.getText());
		longitude					=	longitude.replace("\'", "\'\'");
		longitude					=	longitude.replaceAll("[*]+", "%");
		longitude					=	longitude.replaceAll("[?]", "_");
*/
		//noinspection ConstantConditions
		if (boolShowTrans) {
			tfLastName.setText(last);
			tfFirstName.setText(first);
			tfParty.setText(party);
			tfCity.setText(city);
			tfPrecinct.setText(precinct);
			tfZip.setText(zip);
			tfStreetNo.setText(streetno);
			tfStreetNo.setText(street);
			tfRegAgo.setText(regago.toString());
			tfAgeMin.setText(ageMin);
			tfAgeMax.setText(ageMax);
			tfNumVotes.setText(numvotes.toString());
//			tfLat.setText(latitude);
//			tfLong.setText(longitude);
		}

			/* Build the Where clause */
		if (last.length() > 0 ||
						first.length() > 0 ||
						party.length() > 0 ||
						city.length() > 0 ||
						precinct.length() > 0 ||
						zip.length() > 0 ||
						streetno.length() > 0 ||
						street.length() > 0)
		{

			whereClause = "WHERE ";

			if (last.length() > 0) {
				whereClause += ("voters.szNameLast LIKE '" + last + "' ");
			}
			if (first.length() > 0) {
				if (whereClause.length() > 6) {
					whereClause += "AND ";
				}
				whereClause += ("voters.szNameFirst LIKE '" + first + "' ");
			}
			if (party.length() > 0) {
				if (whereClause.length() > 6) {
					whereClause += "AND ";
				}
				whereClause += ("voters.szPartyName LIKE '" + party + "' ");
			}
			if (city.length() > 0) {
				if (whereClause.length() > 6) {
					whereClause += "AND ";
				}
				whereClause += ("voters.szSitusCity LIKE '" + city + "' ");
			}
			if (precinct.length() > 0) {
				if (whereClause.length() > 6) {
					whereClause += "AND ";
				}
				whereClause += ("voters.sPrecinctID LIKE '" + precinct + "' ");
			}
			if (zip.length() > 0) {
				if (whereClause.length() > 6) {
					whereClause += "AND ";
				}
				whereClause += ("voters.sSitusZip LIKE '" + zip + "' ");
			}
			if (streetno.length() > 0) {
				if (whereClause.length() > 6) {
					whereClause += "AND ";
				}
				whereClause += ("voters.sHouseNum LIKE '" + streetno + "' ");
			}
			if (evenOnly)
			{
				if (whereClause.length() > 6)
				{
					whereClause += "AND ";
				}
				whereClause += ("(voters.sHouseNum%2=0) ");
			} else if (oddOnly)
			{
				if (whereClause.length() > 6)
				{
					whereClause += "AND ";
				}
				whereClause += ("(voters.sHouseNum%2=1) ");
			}
			if(mailVoters){
				if(whereClause.length() > 6)
				{
					whereClause += "AND ";
				}
				if(pollVoters)
				{
					whereClause += "szAVStatusAbbr IN ('OS-TEMP','Perm VBM','') ";
				} else {
					whereClause += "szAVStatusAbbr IN ('OS-TEMP','Perm VBM') ";
				}
			} else {
				if(pollVoters) {
					if(whereClause.length() > 6)
					{
						whereClause += "AND ";
					}
					whereClause += "szAVStatusAbbr IN ('') ";
				}
			}

			if (street.length() > 0) {
				if (whereClause.length() > 6) {
					whereClause += "AND ";
				}
				whereClause += ("voters.szStreetName LIKE '" + street + "' ");
			}

			selectNumVotes = "SELECT " +
							"voters.lVoterUniqueID, " +
							"voters.szNameLast, " +
							"voters.szNameFirst, " +
							"voters.sGender, " +
							"voters.szPhone, " +
							"voters.szSitusAddress, " +
							"voters.szSitusCity, " +
							"voters.sSitusZip, " +
							"voters.sPrecinctID, " +
							"voters.szPartyName, " +
							"dtBirthDate, " +
							"dtOrigRegDate, " +
							"DATEDIFF(CURDATE(), dtBirthDate) " +
							"AS age, " +
							"DATEDIFF(CURDATE(), dtOrigRegDate) " +
							"AS agoreg " +
							"FROM voters " +
							"JOIN history ON history.lVoterUniqueID = voters.lVoterUniqueID ";

			groupBy = "GROUP BY voters.lVoterUniqueID ";

			orderBy = "ORDER BY " +
							"CAST(sPrecinctID as unsigned), szStreetName, " +
							"sStreetSuffix, CAST(sHouseNum as unsigned), " +
							"CAST(sUnitNum as unsigned), szNameLast, " +
							"szNameFirst, dtBirthDate DESC, dtOrigRegDate DESC ";

			if (numvotes < 0 || numvotes == null) { numvotes = 0; tfNumVotes.setText("0"); }
//			if (ageMin.length() == 0) { ageMin = "17"; tfAgeMin.setText("17"); }
			if (ageMax.length() == 0) { ageMax = "130"; tfAgeMax.setText("130"); }
			if (regago < 0 || regago == null) { regago = 0; tfRegAgo.setText("0"); }
//			System.out.println("numvotes = "+ numvotes +", ageMin = "+ ageMin +", ageMax = "+ ageMax +",
// regago = "+ regago);

			having = " HAVING " +
							"count(IF(history.szCountedFlag = 'YES', 1, NULL)) >= " +
							numvotes +
							" AND age/365 >= " +
							ageMin +
							" AND ((age)/365)-1 <= " +
							ageMax +
							" AND agoreg/365 >= " +
							regago;
						if(excludeIfVotedInElection.length() > 0) {
							having += " AND COUNT(CASE WHEN history.sElectionAbbr LIKE '" +
											excludeIfVotedInElection + "' " +
											"THEN 1 ELSE null END) = 0 ";
						}else{
							having += " ";
						}

/*
			SELECT *, voters.lVoterUniqueID, count(IF(history.szCountedFlag = 'YES',1,NULL))
				as votes FROM	voters	join	history ON history.lVoterUniqueID = voters.lVoterUniqueID
			WHERE voters.sPrecinctID LIKE '1402'
			GROUP BY sPrecinctID, szStreetName, sStreetSuffix, CAST(sHouseNum as unsigned),
			CAST(sUnitNum as unsigned), szSitusAddress, voters.lVoterUniqueID
			HAVING votes >= 3
*/

//			System.out.println("selectNumVotes: " + selectNumVotes);
//			System.out.println("whereClause:" + whereClause);
//			System.out.println("groupBy:" + groupBy);
//			System.out.println("having: " + having);
			query = selectNumVotes + whereClause + groupBy + having + orderBy;
			System.out.println("QUERY: " + query);
			resultSet = doQuery(query, voterDB);
		} else
		{
			return null;
		}
		resultSetW = resultSet;
		try
		{
			if (resultSet.absolute(1))
			{
				return resultSet;
			}
		} catch (SQLException e)
		{
			Database.printSQLException( e );
		}
		return null;
	}

	public void getVoters() {
		int rowCount = 0;
		ResultSet resultSet = voterSearch(voterDB);
		if (null != resultSet)
		{
			if (null != historyPane)
			{
				dataPanelVoters.remove(voterPane);
				dataPanel.remove(dataPanelVoters);
				validate();
			}
			vTblModel = new VoterTableModel(resultSet);
			vTbl = new JTable(vTblModel);
			vTbl.setFont(sansTable);
			//noinspection unchecked
			vTbl.setRowSorter(new TableRowSorter(vTblModel));
			vTbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			vTbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			vTbl.setFillsViewportHeight(true);
			voterPane = new JScrollPane();
			voterPane.setPreferredSize(new Dimension(width - cpWidth, height * 4 / 6));
			voterPane.setViewportView(vTbl);

			rowCount = vTbl.getRowCount();
			if (1 == rowCount)
			{
				lblVoterPanel.setText("1 Voter");
			} else
			{
				Integer households = getHouseholds(resultSet);
				lblVoterPanel.setText(String.format(Locale.US, "%,d Voters in %,d Households", rowCount, households));
			}

			dataPanelVoters.add(voterPane);
			dataPanelVoters.validate();
			dataPanel.add(dataPanelVoters, BorderLayout.CENTER);
			dataPanel.validate();

			try
			{
				voterID = (Integer) vTbl.getValueAt(0, 0);  /* Get lVoterUniqueID from first row*/
			} catch (IndexOutOfBoundsException e)
			{
				JOptionPane.showMessageDialog(vdPane,
								"Your search terms resulted in no records found.",
								"Warning", JOptionPane.WARNING_MESSAGE);
				dataPanelVoters.remove(voterPane);
				dataPanel.remove(dataPanelVoters);
				validate();
				tfLastName.requestFocus();
				System.out.println("\nIndexOutOfBoundsException caught at voterID = getValueAt (0,0)");
				e.printStackTrace();
			}
//			System.out.println("voterID = " + voterID);

			//	Triggers a search for the history of
			// the selected voter, when a new voter is selected.
			//
			if (voterIDTrigger != voterID)
			{
				voterIDTrigger = voterID;
				historySearch(voterID, voterDB);
			}
			vTbl.requestFocus();
			try
			{
				vTbl.setRowSelectionInterval(0, 0);  /* Select first row */
			} catch (IndexOutOfBoundsException exc)
			{
				System.out.println("IndexOutOfBoundsException at vTbl.setRowSelectionInterval");
				exc.printStackTrace();
				tfLastName.requestFocus();
			}

			vTbl.setCellSelectionEnabled(false);
			vTbl.setRowSelectionAllowed(true);
			vTbl.setColumnSelectionAllowed(false);

			vTbl.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged( ListSelectionEvent evt ) {
//					System.out.println("Current row selected = " + vTbl.getSelectedRow() + ". ");

					if (vTbl.getSelectedRow() < 0) {vTbl.setRowSelectionInterval(0, 0);}
					row = vTbl.getSelectedRow();
					if ((Integer) vTbl.getValueAt(row, 0) != voterIDTrigger)
					{
						try
						{
							voterID = (Integer) vTbl.getValueAt(row, 0);
//							System.out.println("voterID selected = " + voterID.toString());
						} catch (ArrayIndexOutOfBoundsException exc)
						{
							System.out.println("ArrayIndexOutOfBounds Exception caught at listSelection Listener");
							System.out.println("voterID selected = " + voterID.toString());
							exc.printStackTrace();
						}
						voterIDTrigger = voterID;
						historySearch(voterID, voterDB);
					}
				}
			});
		}
	}

	public Integer getHouseholds(ResultSet resultSet) {
		String thisAddress, prevAddress = "";
		Integer households = 0;
		try
		{
			resultSet.beforeFirst();
			while (resultSet.next())
			{
				thisAddress = resultSet.getString(6);
//				System.out.println("thisAddress: " + thisAddress + ", prevAddress: " + prevAddress);
				if (!prevAddress.equals(thisAddress))
				{
					households += 1;
					prevAddress = thisAddress;
				}
			}
		} catch (SQLException e)
		{
			System.out.println("SQL Exception caught at " +
							"PrintWalkList/getCounts.");
			Database.printSQLException( e );
			return -1;
		}
		return households;
	}

	public void print() {

	}

	public void help() {

	}

	protected ResultSet doQuery( String query, Database voterDB ) {
		voterDB.dbQuery(query);
		return voterDB.getResultSet();
	}

	private ResultSet doQueryH(String queryH, Database voterDB) {
		voterDB.dbQueryH(queryH);
		return voterDB.getResultSetH();
	}

	@Override
	public void keyTyped( KeyEvent e ) {
		if (e.getSource().toString().contains("javax.swing.JTextField"))
		{
			if (e.getKeyChar() == KeyEvent.VK_ENTER)
			{
				this.getVoters();
			}
		}
	}

	@Override
	public void keyPressed( KeyEvent e ) {

	}

	@Override
	public void keyReleased( KeyEvent e ) {

	}

	@Override
	public void sorterChanged( RowSorterEvent e ) {
		vTbl.requestFocus();
//		System.out.println("Sorter changed, current row selected = " + vTbl.getSelectedRow());
		if (vTbl.getSelectedRow() < 0) {
			vTbl.setRowSelectionInterval(0, 0);
			historySearch((Integer) vTbl.getValueAt(0, 0), voterDB);
		}
	}

}
/*
Wildcards:
http://dev.mysql.com/doc/refman/5.0/en/string-comparison-functions.html

Selects voters with over 3 votes, and sorts down to address by unit number cast as unsigned[integer]:
SELECT
    *,
voters.lVoterUniqueID,
    count(IF(history.szCountedFlag = 'YES',
        1,
        NULL)) as votes
FROM
    voters
        join
    history ON history.lVoterUniqueID = voters.lVoterUniqueID
WHERE
    voters.sPrecinctID LIKE '1402'
GROUP BY sPrecinctID, szStreetName, sStreetSuffix, CAST(sHouseNum as unsigned), CAST(sUnitNum as unsigned),
szSitusAddress, voters.lVoterUniqueID
HAVING votes >= 3



Registered how long ago, Number of votes, Age

SANTA CLARA COUNTY FIELDS:

 VOTERS:

lVoterUniqueID
sAffNumber
szStateVoterID
sVoterTitle
szNameLast
szNameFirst
szNameMiddle
sNameSuffix
sGender
szSitusAddress
szSitusCity
sSitusState
sSitusZip
sHouseNum
sUnitAbbr
sUnitNum
szStreetName
sStreetSuffix
sPreDir
sPostDir
szMailAddress1
szMailAddress2
szMailAddress3
szMailAddress4
szMailZip
szPhone
szEmailAddress
dtBirthDate
sBirthPlace
dtRegDate
dtOrigRegDate
dtLastUpdate_dt
sStatusCode
szStatusReasonDesc
sUserCode1
sUserCode2
iDuplicateIDFlag
szLanguageName
szPartyName
szAVStatusAbbr
szAVStatusDesc
szPrecinctName
sPrecinctID
sPrecinctPortion
sDistrictID_0
iSubDistrict_0
szDistrictName_0
sDistrictID_1
iSubDistrict_1
szDistrictName_1
sDistrictID_2
iSubDistrict_2
szDistrictName_2
sDistrictID_3
iSubDistrict_3
szDistrictName_3
sDistrictID_4
iSubDistrict_4
szDistrictName_4
sDistrictID_5
iSubDistrict_5
szDistrictName_5

 HISTORY:

lVoterUniqueID
sElectionAbbr
szElectionDesc
dtElectionDate
sElecTypeDesc
sVotingPrecinct
szVotingMethod
sPartyAbbr
szPartyName
szCountedFlag

 */
