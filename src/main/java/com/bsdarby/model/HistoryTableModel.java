package com.bsdarby.model;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

/* class VoterTableModel */
/**
 * Class in voterdata2/com.bsdarby.model.
 * Created by bsdarby on 9/14/14.
 * This class provides methods for displaying the results returned from the History table.
 * The methods are used by a JTable object so the results display in a cell format.
 *
 * @author Brian Darby
 * @version 1.1
 */
public class HistoryTableModel extends AbstractTableModel {
	//...The result set from the Voters Table to be displayed
	public final ResultSet resultSetH;

	/* ListingsTableModel */

	/**
	 * The VoterTableModel constructor
	 *
	 * @param resultSetH ...the ResultSet from the Listings table to be displayed
	 */
	public HistoryTableModel( ResultSet resultSetH ) {
		this.resultSetH = resultSetH;
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
			resultSetH.last();
			return resultSetH.getRow();
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
	 * This method always returns 8 for .
	 * <pre>
	 * PRE:		true
	 * POST:	The number 8 is returned.
	 * </pre>
	 *
	 * @return 7 ...the number 7 is returned, for the seven output columns
	 * VoterID, Last Name, First Name, Gender, Phone Number,
	 * Full Address, City, Precinct, and Party.
	 */
	public int getColumnCount() {
		return 7;
	}

	/* getColumnName */
	/*
	lVoterUniqueID,
	sElectionAbbr,
	szElectionDesc
	dtElectionDate
	sElecTypeDesc
	sVotingPrecinct
	szVotingMethod
	sPartyAbbr
	szPartyName
	szCountedFlag */

	/**
	 * Returns the name of the column specified by the index.
	 * <pre>
	 * PRE:		Column is assigned and 0 >= column <= 8.
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
			String colName = resultSetH.getMetaData().getColumnName(column + 1);
			if (colName.equals("lVoterUniqueID"))
			{
				return "VoterID";
			} else if (colName.equals("sElectionAbbr"))
			{
				return "Election";
			} else if (colName.equals("szElectionDesc"))
			{
				return "Description";
			} else if (colName.equals("dtElectionDate"))
			{
				return "Election Date";
			} else if (colName.equals("sElecTypeDesc"))
			{
				return "Type";
			} else if (colName.equals("sVotingPrecinct"))
			{
				return "VPrecinct";
			} else if (colName.equals("szVotingMethod"))
			{
				return "Vote Method";
			} else if (colName.equals("sPartyAbbr"))
			{
				return "Party";
			} else if (colName.equals("szPartyName"))
			{
				return "Party";
			} else if (colName.equals("szCountedFlag"))
			{
				return "Voted";
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
	public Object getValueAt( int row, int column ) {
		try
		{
			resultSetH.absolute(row + 1);
			return resultSetH.getObject(column + 1);
		} catch (SQLException e)
		{
			System.out.println("SQL  Exception caught in HistoryTableModel/getValueAt.");
			DatabaseManager.printSQLException(e);
			return null;
		}
	}
}
