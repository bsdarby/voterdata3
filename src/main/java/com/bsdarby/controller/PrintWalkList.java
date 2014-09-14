package com.bsdarby.controller;

import com.bsdarby.model.DatabaseManager;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;

/**
 * Class in voterdata2/com.bsdarby.controller.
 * Created by bsdarby on 9/14/14.
 *
 * Originally PrintTableDemo - author Shannon Hickey
 */
public class PrintWalkList extends JFrame {

	private static ResultSet resultSet;

	/* UI Components */
	private JPanel contentPane;
	private JLabel lblWalkList;
	private JTable tblWalking;
	private JScrollPane scrollPane;
	private JCheckBox showPrintDialogBox;
	private JCheckBox interactiveBox;
	private JCheckBox fitWidthBox;
	private JButton printButton;

	/* Protected so that they can be modified/disabled by subclasses */
	protected JCheckBox headerBox;
	protected JCheckBox footerBox;
	protected JTextField headerField;
	protected JTextField footerField;
	protected static int rsetColumns;
	protected static TableColumn column;
	static int households;
	static int voters;
	static String precinct;

	/**
	 * Constructs an instance of the print utility.
	 */
	public PrintWalkList( ResultSet resultSet, String daprecinct ) {
		super("Print Walk List");
		this.resultSet = resultSet;
		setPreferredSize(new Dimension(VoterDataUI.width - 50, VoterDataUI.height - 100));
		setLocationRelativeTo(null);
		precinct = daprecinct;

		try
		{
			rsetColumns = resultSet.getMetaData().getColumnCount();

		} catch (SQLException e)
		{
			System.out.println("SQL Exception caught at WalkModel constructor.");
			e.printStackTrace();
		}

		Font fontTable = new Font("Bitstream Vera Sans Mono Roman", Font.PLAIN, 14);

		lblWalkList = new JLabel("Walking List");
		lblWalkList.setFont(fontTable);
//		lblWalkList.setFont(new Font("Times New Roman Bold", Font.PLAIN, 14));

		WalkModel mdlWalking = new WalkModel(resultSet);
		tblWalking = createTable(mdlWalking);
//		mdlWalking.addBlankColumn("Data");
		tblWalking.setFillsViewportHeight(true);
		tblWalking.setRowHeight(24);
		tblWalking.setFont(fontTable);
		tblWalking.setBorder(null);

		tblWalking.addColumn(tblWalking.getColumnModel().getColumn(8));
		tblWalking.getColumnModel().getColumn(8).setPreferredWidth(10);
		tblWalking.removeColumn(tblWalking.getColumnModel().getColumn(8));

		tblWalking.addColumn(tblWalking.getColumnModel().getColumn(5));
		tblWalking.getColumnModel().getColumn(5).setPreferredWidth(150);
		tblWalking.removeColumn(tblWalking.getColumnModel().getColumn(5));

		tblWalking.addColumn(tblWalking.getColumnModel().getColumn(5));
		tblWalking.getColumnModel().getColumn(5).setPreferredWidth(30);
		tblWalking.removeColumn(tblWalking.getColumnModel().getColumn(5));

		tblWalking.addColumn(tblWalking.getColumnModel().getColumn(5));
		tblWalking.getColumnModel().getColumn(5).setPreferredWidth(15);
		tblWalking.removeColumn(tblWalking.getColumnModel().getColumn(5));

		tblWalking.addColumn(tblWalking.getColumnModel().getColumn(2));
		tblWalking.getColumnModel().getColumn(2).setPreferredWidth(45);
		tblWalking.removeColumn(tblWalking.getColumnModel().getColumn(2));

		tblWalking.addColumn(tblWalking.getColumnModel().getColumn(1));
		tblWalking.getColumnModel().getColumn(1).setPreferredWidth(60);
		tblWalking.removeColumn(tblWalking.getColumnModel().getColumn(1));

		tblWalking.addColumn(tblWalking.getColumnModel().getColumn(1));
		tblWalking.getColumnModel().getColumn(1).setPreferredWidth(3);
		tblWalking.removeColumn(tblWalking.getColumnModel().getColumn(1));

		tblWalking.addColumn(tblWalking.getColumnModel().getColumn(3));
		tblWalking.getColumnModel().getColumn(1).setPreferredWidth(3);
		tblWalking.removeColumn(tblWalking.getColumnModel().getColumn(3));

		tblWalking.addColumn(tblWalking.getColumnModel().getColumn(3));
		tblWalking.getColumnModel().getColumn(1).setPreferredWidth(3);
		tblWalking.removeColumn(tblWalking.getColumnModel().getColumn(3));

		tblWalking.addColumn(tblWalking.getColumnModel().getColumn(2));
		tblWalking.getColumnModel().getColumn(1).setPreferredWidth(60);
		tblWalking.removeColumn(tblWalking.getColumnModel().getColumn(2));

		tblWalking.addColumn(tblWalking.getColumnModel().getColumn(5));
		tblWalking.getColumnModel().getColumn(1).setPreferredWidth(135);
		tblWalking.removeColumn(tblWalking.getColumnModel().getColumn(5));

		tblWalking.removeColumn(tblWalking.getColumnModel().getColumn(0));
		tblWalking.removeColumn(tblWalking.getColumnModel().getColumn(0));
		tblWalking.removeColumn(tblWalking.getColumnModel().getColumn(0));
		tblWalking.removeColumn(tblWalking.getColumnModel().getColumn(0));
		tblWalking.removeColumn(tblWalking.getColumnModel().getColumn(0));

		tblWalking.validate();

        /* Set a custom renderer on the "Passed" column */
//		tblWalking.getColumn("Passed").setCellRenderer(createPassedColumnRenderer());

		scrollPane = new JScrollPane(tblWalking);

		String tooltipText;

		tooltipText = "Include a page header";
		headerBox = new JCheckBox("Header:", true);
		headerBox.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent ae ) {
				headerField.setEnabled(headerBox.isSelected());
			}
		});
		headerBox.setToolTipText(tooltipText);
		tooltipText = "Page Header (Use {0} to include page number)";
		headerField = new JTextField(" Page {0}");
		headerField.setToolTipText(tooltipText);

		tooltipText = "Include a page footer";
		footerBox = new JCheckBox("Footer:", false);
		footerBox.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent ae ) {
				footerField.setEnabled(footerBox.isSelected());
			}
		});
		footerBox.setToolTipText(tooltipText);
		tooltipText = "Page Footer (Use {0} to Include Page Number)";
		footerField = new JTextField("Page {0}");
		footerField.setToolTipText(tooltipText);

		tooltipText = "Show the Print Dialog Before Printing";
		showPrintDialogBox = new JCheckBox("Show print dialog", true);
		showPrintDialogBox.setToolTipText(tooltipText);
		showPrintDialogBox.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent ae ) {
				if (!showPrintDialogBox.isSelected())
				{
					JOptionPane.showMessageDialog(
									PrintWalkList.this,
									"If the Print Dialog is not shown,"
													+ " the default printer is used.",
									"Printing Message",
									JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});


		tooltipText = "Show a Status Dialog During Printing";
		interactiveBox = new JCheckBox("Interactive (Show status dialog)", true);
		interactiveBox.setToolTipText(tooltipText);
		interactiveBox.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent ae ) {
				if (!interactiveBox.isSelected())
				{
					JOptionPane.showMessageDialog(
									PrintWalkList.this,
									"If non-interactive, the GUI is fully blocked"
													+ " during printing.",
									"Printing Message",
									JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		tooltipText = "Shrink the Table to Fit the Entire Width on a Page";
		fitWidthBox = new JCheckBox("Fit width to printed page", true);
		fitWidthBox.setToolTipText(tooltipText);

		tooltipText = "Print the Table";
		printButton = new JButton("Print");
		printButton.setToolTipText(tooltipText);

		printButton.addActionListener(new ActionListener() {
			public void actionPerformed( ActionEvent ae ) {
				printTable();
			}
		});

		contentPane = new JPanel();
		addComponentsToContentPane();
		setContentPane(contentPane);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(700, 600);
		setLocationRelativeTo(null);
		validate();
		setVisible(true);
	}

	public void getCounts() {
		String thisAddress, prevAddress = "";
		voters = 0;
		households = 0;
		try
		{
			resultSet.beforeFirst();
			while (resultSet.next())
			{
				voters += 1;
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
			DatabaseManager.printSQLException(e);
		}
	}

	/**
	 * Adds to and lays out all GUI components on the {@code contentPane} panel.
	 * <p/>
	 * It is recommended that you <b>NOT</b> try to understand this code. It was
	 * automatically generated by the NetBeans GUI builder.
	 */
	private void addComponentsToContentPane() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(BorderFactory.createTitledBorder("Printing"));

		GroupLayout bottomPanelLayout = new GroupLayout(bottomPanel);
		bottomPanel.setLayout(bottomPanelLayout);
		bottomPanelLayout.setHorizontalGroup(
						bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(bottomPanelLayout.createSequentialGroup()
														.addContainerGap()
														.addGroup(bottomPanelLayout.createParallelGroup(GroupLayout
																		.Alignment.LEADING)
																		.addComponent(headerBox)
																		.addComponent(footerBox))
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(bottomPanelLayout.createParallelGroup(GroupLayout
																		.Alignment.LEADING, false)
																		.addComponent(footerField)
																		.addComponent(headerField,
																						GroupLayout.DEFAULT_SIZE, 180,
																						Short.MAX_VALUE))
														.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
														.addGroup(bottomPanelLayout.createParallelGroup(GroupLayout
																		.Alignment.LEADING, false)
																		.addGroup(bottomPanelLayout
																						.createSequentialGroup()
																						.addComponent(fitWidthBox)
																						.addPreferredGap(LayoutStyle
																														.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
																										Short
																														.MAX_VALUE)
																						.addComponent(printButton))
																		.addGroup(bottomPanelLayout
																						.createSequentialGroup()
																						.addComponent
																										(showPrintDialogBox)
																						.addPreferredGap(LayoutStyle
																										.ComponentPlacement.RELATED)
																						.addComponent(interactiveBox)))
														.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		bottomPanelLayout.setVerticalGroup(
						bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(bottomPanelLayout.createSequentialGroup()
														.addGroup(bottomPanelLayout.createParallelGroup(GroupLayout
																		.Alignment.BASELINE)
																		.addComponent(headerBox)
																		.addComponent(headerField,
																						GroupLayout.PREFERRED_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.PREFERRED_SIZE)
																		.addComponent(interactiveBox)
																		.addComponent(showPrintDialogBox))
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(bottomPanelLayout.createParallelGroup(GroupLayout
																		.Alignment.BASELINE)
																		.addComponent(footerBox)
																		.addComponent(footerField,
																						GroupLayout.PREFERRED_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.PREFERRED_SIZE)
																		.addComponent(fitWidthBox)
																		.addComponent(printButton))
														.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		GroupLayout layout = new GroupLayout(contentPane);
		contentPane.setLayout(layout);
		layout.setHorizontalGroup(
						layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
														.addContainerGap()
														.addGroup(layout.createParallelGroup(GroupLayout.Alignment
																		.LEADING)
																		.addComponent(scrollPane,
																						GroupLayout.DEFAULT_SIZE, 486,
																						Short.MAX_VALUE)
																		.addComponent(lblWalkList)
																		.addComponent(bottomPanel,
																						GroupLayout.PREFERRED_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.PREFERRED_SIZE))
														.addContainerGap())
		);
		layout.setVerticalGroup(
						layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
														.addContainerGap()
														.addComponent(lblWalkList)
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 262,
																		Short.MAX_VALUE)
														.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)
														.addContainerGap())
		);
	}

	/**
	 * Create and return a table for the given model.
	 * <p/>
	 * This is protected so that a subclass can return an instance
	 * of a different {@code JTable} subclass. This is interesting
	 * only for {@code TablePrintDemo3} where we want to return a
	 * subclass that overrides {@code getPrintable} to return a
	 * custom {@code Printable} implementation.
	 */
	protected JTable createTable( TableModel model ) {
		return new JTable(model);
	}

	/**
	 * Create and return a cell renderer for rendering the pass/fail column.
	 * This is protected so that a subclass can further customize it.
	 */
	protected TableCellRenderer createPassedColumnRenderer() {
		return new PassedColumnRenderer();
	}

	/**
	 * Print the table.
	 */
	private void printTable() {
		getCounts();
				/* Fetch printing properties from the GUI components */

		MessageFormat header = null;

        /* if we should print a header */
//		if (headerBox.isSelected())
//		{
						/* create a MessageFormat around the header text */
		header = new MessageFormat("Precinct " + precinct + " List – " + voters + " voters – " + households + " " +
						"households" +
						" – Page {0}");
//			header = new MessageFormat(headerField.getText());
//		}

		MessageFormat footer = null;

        /* if we should print a footer */
		if (footerBox.isSelected())
		{
						/* create a MessageFormat around the footer text */
			footer = new MessageFormat(footerField.getText());
		}

		boolean fitWidth = fitWidthBox.isSelected();
		boolean showPrintDialog = showPrintDialogBox.isSelected();
		boolean interactive = interactiveBox.isSelected();

        /* determine the print mode */
		JTable.PrintMode mode = fitWidth ? JTable.PrintMode.FIT_WIDTH
						: JTable.PrintMode.NORMAL;

		try
		{
						/* print the table */
			boolean complete = tblWalking.print(mode, header, footer,
							showPrintDialog, null,
							interactive, null);

            /* if printing completes */
			if (complete)
			{
								/* show a success message */
				JOptionPane.showMessageDialog(this,
								"Printing Complete",
								"Printing Result",
								JOptionPane.INFORMATION_MESSAGE);
			} else
			{
								/* show a message indicating that printing was cancelled */
				JOptionPane.showMessageDialog(this,
								"Printing Cancelled",
								"Printing Result",
								JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (PrinterException pe)
		{
						/* Printing failed, report to the user */
			JOptionPane.showMessageDialog(this,
							"Printing Failed: " + pe.getMessage(),
							"Printing Result",
							JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Start the application.
	 */
	public static void main( final String[] args ) {
				/* Schedule for the GUI to be created and shown on the EDT */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
								/* Don't want bold fonts if we end up using metal */
				UIManager.put("swing.boldMetal", false);
				try
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e)
				{
					System.out.println("Exception caught in main.");
					e.printStackTrace();
				}
				new PrintWalkList(resultSet, precinct).setVisible(true);
			}
		});
	}

	/**
	 * A table model containing walking data.
	 */
	private static class WalkModel extends AbstractTableModel {

		private WalkModel( ResultSet resultSet ) {
//			System.out.println("Columns in model    = " + this.getColumnCount());
//			System.out.println("Columns in resultSet = " + rsetColumns);

		}

		public void setValueAt( Object aValue, int rowIndex, int columnIndex ) {}

		public void addTableModelListener( TableModelListener l ) {}

		public void removeTableModelListener( TableModelListener l ) {}

		public boolean isCellEditable( int rowIndex, int columnIndex ) {
			return false;
		}


/*		public void addBlankColumn(String columnName) {
			ArrayList headers = new ArrayList();

			headers.add(columnName);
			colCount = headers.size();
			if (columnData != null) {
				for (int r = 0; r < localCache.size(); r++) {
					((List)localCache.get(r)).add(columnData.get(r));
				}
			} else {
				System.out.println("Null columnData passed");
			}
			fireTableStructureChanged();
		}
*/

		@Override
		public Class<?> getColumnClass( int column ) {
			switch (column)
			{
				case 0:
				case 1:
					return int.class;
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
					return String.class;
				case 12:
					return int.class;
				case 13:
				case 14:
				case 15:
					return String.class;
			}

			throw new AssertionError("invalid column");
		}

		/* getValueAt */

		/**
		 * Returns the value in the ResultSet at the location specified by row and column.
		 * <pre>
		 * PRE:		row and column are assigned and 0 >= column <= 2 and row is within range.
		 * POST:	The value in the ResultSet at row and column is returned, or the combined
		 * 			phone number is returned if column = 2.
		 * </pre>
		 *
		 * @param row    ...the row of the ResultSet whose value is to be returned.
		 * @param column ...the column of the ResultSet whose value is to be returned.
		 * @return ...the value in the ResultSet at row and column is returned.
		 */
		public Object getValueAt( int row, int column ) {

			String thisAddress;
			String prevAddress;
			try
			{
				resultSet.absolute(row + 1);
			} catch (SQLException e)
			{
				DatabaseManager.printSQLException(e);
				return null;
			}

			switch (column)
			{
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
					try
					{
						return resultSet.getObject(column + 1);
					} catch (SQLException e)
					{
						System.out.println("SQL Exception caught at " +
										"PrintWalkList/getValueAt/switch.");
						DatabaseManager.printSQLException(e);
						return null;
					}
				case 5:
					try
					{
						if (row > 0)
						{
							thisAddress = resultSet.getString(column + 1);
							resultSet.absolute(row);
							prevAddress = resultSet.getString(column + 1);
							resultSet.absolute(row + 1);
							if (prevAddress.equals(thisAddress))
							{
//								System.out.println(thisAddress + " = previousAddress");
								return null;
							} else
							{
//								System.out.println(thisAddress + " = new address");
//								prevAddress = thisAddress;
								return thisAddress;
							}
						} else
						{
							return resultSet.getObject(column + 1);
						}
					} catch (SQLException e)
					{
						System.out.println("SQL Exception caught at " +
										"PrintWalkList/getValueAt/switch.");
						DatabaseManager.printSQLException(e);
						return null;
					}
				case 6:
				case 7:
				case 8:
				case 9:
					try
					{
						return resultSet.getObject(column + 1);
					} catch (SQLException e)
					{
						System.out.println("SQL Exception caught at " +
										"PrintWalkList/getValueAt/switch.");
						DatabaseManager.printSQLException(e);
						return null;
					}
				case 10:
				case 11:
					try
					{
						if (null != resultSet.getObject(column + 1))
						{
							return DatabaseManager.years((Date) (resultSet.getObject(column + 1)));
						}
					} catch (SQLException e)
					{
						System.out.println("SQL Exception caught at " +
										"PrintWalkList/getValueAt/switch.");
						DatabaseManager.printSQLException(e);
						return null;
					}
				case 12:
				case 13:
				case 14:
					try
					{
						return resultSet.getObject(column + 1);
					} catch (SQLException e)
					{
						System.out.println("SQL Exception caught at " +
										"PrintWalkList/getValueAt/switch.");
						DatabaseManager.printSQLException(e);
						return null;
					}
				case 15:
					return "Yes No UnD NH YS Do";

			}
			System.out.println("Error: Column = " + column + 1);
			throw new AssertionError("invalid column");
		}

		public int getRowCount() {
			try
			{
				resultSet.last();
				return resultSet.getRow();
			} catch (SQLException e)
			{
				e.printStackTrace();
				return 0;
			}
		}

	/* getColumnCount */

		/**
		 * Returns the number of columns to be displayed for the ResultSet.  Note that
		 * this does not return the number of columns IN the ResultSet.
		 * <pre>
		 * PRE:		true
		 * POST:	The number 10 is returned.
		 * </pre>
		 *
		 * @return 10 ...the number 10 is returned.
		 */
		public int getColumnCount() {
			return rsetColumns + 1;
		}

		/* getColumnName */
		/* lVoterUniqueID, szNameLast, szNameFirst, szPhone, sHouseNum, szStreetName, sStreetSuffix, sUnitNum,
		sPrecinctID, szPartyName */

		/**
		 * Returns the name of the column specified by the index.
		 * <pre>
		 * PRE:		Column is assigned and 0 >= column <= 6.
		 * POST:	A column name is returned,
		 * </pre>
		 *
		 * @param column ...the index of the column name to be returned.
		 * @return column name ...the name of the column is returned.
		 */
		public String getColumnName( int column ) {
			String colName = "";
			try
			{
				if (rsetColumns >= column + 1)
				{
					colName = resultSet.getMetaData().getColumnName(column + 1);
					if (colName.equals("lVoterUniqueID"))
					{
						return "VoterID";
					} else if (colName.equals("szNameLast"))
					{
						return "Last Name";
					} else if (colName.equals("szNameFirst"))
					{
						return "First Name";
					} else if (colName.equals("szPhone"))
					{
						return "Phone Number";
					} else if (colName.equals("sGender"))
					{
						return "M/F";
					} else if (colName.equals("szSitusAddress"))
					{
						return "Street Address";
					} else if (colName.equals("sHouseNum"))
					{
						return "StNo";
					} else if (colName.equals("szStreetName"))
					{
						return "Street";
					} else if (colName.equals("sStreetSuffix"))
					{
						return "Sfx";
					} else if (colName.equals("sUnitNum"))
					{
						return "#";
					} else if (colName.equals("sSitusZip"))
					{
						return "Zip";
					} else if (colName.equals("szSitusCity"))
					{
						return "City";
					} else if (colName.equals("sPrecinctID"))
					{
						return "Precinct";
					} else if (colName.equals("szPartyName"))
					{
						return "Party";
					} else if (colName.equals("dtBirthDate"))
					{
						return "Age";
					} else if (colName.equals("dtOrigRegDate"))
					{
						return "Reg'd";
					} else if (colName.equals(""))
					{
						return "Data";
					}
				} else if (rsetColumns < column + 1)
				{
					return "Data";
				}

			} catch (SQLException e)
			{
				e.printStackTrace();
				return "";
			}
			return colName;
		}
	}

	/**
	 * A custom cell renderer for rendering the "Passed" column.
	 */
	protected static class PassedColumnRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent( JTable table,
																										Object value,
																										boolean
																														isSelected,
																										boolean
																														hasFocus,
																										int row,
																										int column ) {

			super.getTableCellRendererComponent(table, value, isSelected,
							hasFocus, row, column);

			setText("");
			setHorizontalAlignment(SwingConstants.CENTER);

            /* set the icon based on the passed status */
			boolean status = (Boolean) value;
//			setIcon(status ? passedIcon : failedIcon);

			return this;
		}
	}
}
