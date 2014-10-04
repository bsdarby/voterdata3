package com.bsdarby.model;


import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/* class VoterTableModel */

/**
 * Class in voterdata2/com.bsdarby.model.
 * Created by bsdarby on 9/14/14.
 * This class provides methods for displaying the results returned from the Voters table
 * for mailing.
 * The methods are used by a JTable object so the results display in a cell format.
 *
 * @author Brian Darby
 * @version 1.1
 */
public class MailingTableModel extends AbstractTableModel {
	//...The result set from the Voters Table to be displayed
	public final ResultSet resultSet;

	/* ListingsTableModel */

	/**
	 * The VoterTableModel constructor
	 *
	 * @param resultSet ...the ResultSet from the Listings table to be displayed
	 */
	public MailingTableModel( ResultSet resultSet ) {
		this.resultSet = resultSet;
	}

	/* getRowCount */

	/**
	 * Returns the number of rows in the ResultSet.
	 * <pre>
	 * PRE:		true
	 * POST:	The number of rows in the ResultSet is returned.
	 * </pre>
	 *
	 * @return ...the number of rows in the ResultSet.
	 */
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
	 * This method always returns 8 for Last Name, First Name, Phone,
	 * Street Number, Street, Unit Number, Precinct, and Party.
	 * <pre>
	 * PRE:		true
	 * POST:	The number 10 is returned.
	 * </pre>
	 *
	 * @return 10 ...the number 10 is returned, for the ten output columns
	 * VoterID, Last Name, First Name, Gender, Phone Number,
	 * Full Address, City, Precinct, and Party.
	 */
	public int getColumnCount() {
		return 10;
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
	public String getColumnName( int column ) {  /*...Returns column names that look better
																									than the database
																									names */
		try
		{
			String colName = resultSet.getMetaData().getColumnName(column + 1);
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
			} else
			{  //...Should never get here
				return colName;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			return "";
		}
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
/*	public Object getValueAt( int row, int column ) {
		try
		{
			resultSet.absolute(row + 1);
			return resultSet.getObject(column + 1);
		} catch (SQLException e)
		{
			DatabaseManager.printSQLException(e);
//			e.printStackTrace();
			return null;
		}
	}
	*/

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
			case 5:
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
									"VoterTableModel/getValueAt/switch.");
					DatabaseManager.printSQLException(e);
					return null;
				}
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
				try
				{
					if (null != resultSet.getObject(column + 1))
					{
						return DatabaseManager.years((Date) (resultSet.getObject(column + 1)));
					}
				} catch (SQLException e)
				{
					System.out.println("SQL Exception caught at " +
									"VoterTableModel/getValueAt/switch.");
					DatabaseManager.printSQLException(e);
					return null;
				}

		}
		System.out.println("Error: Column = " + column + 1);
		throw new AssertionError("invalid column");
	}
}

