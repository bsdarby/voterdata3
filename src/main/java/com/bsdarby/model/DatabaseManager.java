package com.bsdarby.model;

import javax.swing.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Class in voterdata2/com.bsdarby.model.
 * Created by bsdarby on 9/14/14.
 */
public class DatabaseManager {
	/**
	 * A connection to a database
	 */
	private Connection conn;
	/**
	 * An executable SQL statement
	 */
	private Statement stmt;
	/**
	 * The result of an executed SQL statement
	 */
	private ResultSet rset;
	private ResultSet rsetH;
	private PreparedStatement preps = null;
	private PreparedStatement prepsH = null;

	/* DatabaseManager Constructor */

	/**
	 * This constructor connects to the MySQL database at jdbc:mysql://sql.useractive.com:3306.
	 * It creates instances of Statement and ResultSet that will be used by the other methods
	 * in the class.  It also checks to see if the "listings" table already exists.  If it does,
	 * it simply returns to the caller.  Otherwise, it instantiates the method to create a
	 * table called "listings", and then populates the table.
	 * <pre>
	 * PRE:		MySQL server is available and account for user has been established.
	 * 				Database 'voterdata' has been created.
	 * 		 		The MySQL driver is installed on the client workstation and its location
	 * 				has been defined in CLASSPATH (or for Eclipse, in its Referenced Libraries).
	 * 				Username and password are not null.
	 * POST:	A connection is made and the "voters" and "history" tables exist
	 * 				jdbc:mysql://localhost:3306/voterdata.
	 * </pre>
	 */
	public DatabaseManager( String username, String password ) {
		if (username == "CANCELED" && password == "CANCELED")
		{
			System.exit(0);
		}

			/*	Connect to database */
		JFrame dbErrFrame = new JFrame();
		try
		{
			Class.forName("com.mysql.jdbc.Driver");  //...Load the MySQL JDBC driver
		} catch (ClassNotFoundException e)
		{
			System.out.println("Failed to load JDBC/ODBC driver: " + e.getMessage());
			e.printStackTrace();
			JOptionPane.showMessageDialog(dbErrFrame,
							"There was an error in the database driver.\n"
											+ "Please contact your network admin for a solution.");
			System.exit(1);
		}
			/* Connect to the database */
		try
		{
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/voterdata", username, password);
		} catch (SQLException e)
		{
			System.out.println("Failed to login to database." + e.getMessage());
			printSQLException(e);
			JOptionPane.showMessageDialog(dbErrFrame,
							"There was an error logging in to the database.\n"
											+ "Please make sure that the database has been created,\n"
											+ "and check the username and password you are using.");
			System.exit(2);
		}

		try
		{
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

				/*	Execute the creation and initialization of table query */
			DatabaseMetaData aboutDB = conn.getMetaData();
			String[] tableType = { "TABLE" };
				/* test for voters table */
			ResultSet rs = aboutDB.getTables(null, null, "voters", tableType);
			if (inspectForTable(rs, "voters"))
			{ 		/* Find out if the tables exist */
//				System.out.println("The 'voters' table exists");
			} else
			{
				JOptionPane.showMessageDialog(dbErrFrame,
								"There was an error in the database.\n"
												+ "Please contact your administrator.\n"
												+ "The voters table was not found.");
				System.exit(3);
			}
				/* test for history table */
			ResultSet rs2 = aboutDB.getTables(null, null, "history", tableType);
			if (inspectForTable(rs2, "history"))
			{
//				System.out.println("The 'history' table exists");
			} else
			{
				JOptionPane.showMessageDialog(dbErrFrame,
								"There was an error in the database.\n"
												+ "Please contact your administrator.\n"
												+ "The history table was not found.");
				System.exit(3);
			}

		} catch (SQLException e)
		{
			printSQLException(e);
			System.out.println("There was a problem with the database tables: " + e.getMessage());
		}
	}

	/* inspectForTable */

	/**
	 * Determines if a table exists in the db.
	 * <pre>
	 * PRE:		Connection to database has been established. rs is not null.
	 * POST:	Table has not been changed, but its presence is verified (or not).
	 * </pre>
	 *
	 * @param rs        - ResultSet from DatabaseMetaData query about existing tables
	 * @param tableName - String identifying the table in question
	 * @return boolean
	 * @throws SQLException
	 */
	private boolean inspectForTable( ResultSet rs, String tableName ) throws SQLException {
		int i;
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();

		boolean more = rs.next();
		while (more)
		{
			for (i = 1; i <= numCols; i++)
			{
				//noinspection StringEquality
				if (rsmd.getColumnLabel(i) == "TABLE_NAME")
				{
					if (rs.getString(i).equals(tableName))
					{
						return true;
					}
				}
			}
			System.out.println("");
			more = rs.next();    //...Fetch the next result set row
		}
		return false;
	}

	/* doVoterQuery */

	/**
	 * Executes the select query specified.
	 * <pre>
	 * PRE:		Connection to the database has been established. Query is assigned and is a simple
	 * 				prepared statement against the Voters table.
	 * POST:	The query is executed.
	 * </pre>
	 *
	 * @param query - a simple select query against a table
	 */
	public void dbQuery( String query ) {
		try
		{
			conn.setAutoCommit(true);
			preps = conn.prepareStatement(query);
			rset = preps.executeQuery();
		} catch (SQLException e)
		{
			printSQLException(e);
		}
	}

	/* Do History Query */

	/**
	 * Executes the select query specified.
	 * <pre>
	 * PRE:		Connection to the database has been established. Query is assigned and is a simple
	 * 				prepared statement against the History table.
	 * POST:	The query is executed.
	 * </pre>
	 *
	 * @param query - a simple select query against a table
	 */
	public void dbQueryH( String query ) {
		try
		{
			conn.setAutoCommit(true);
			prepsH = conn.prepareStatement(query);
			rsetH = prepsH.executeQuery();
		} catch (SQLException e)
		{
			printSQLException(e);
		}
	}



	/* getResultSet */

	/**
	 * Returns the value of the ResultSet instance
	 * <pre>
	 * PRE:		True
	 * POST:	ResultSet instance value is returned, its value remains the same as upon entry.
	 * </pre>
	 *
	 * @return rset - an existing ResultSet
	 */
	public ResultSet getResultSet() {
		return rset;
	}

	/* close */

	/**
	 * Returns the value of the ResultSet instance
	 * <pre>
	 * PRE:		True
	 * POST:	ResultSet instance value is returned, its value remains the same as upon entry.
	 * </pre>
	 *
	 * @return rset - an existing ResultSet
	 */
	public ResultSet getResultSetH() {
		return rsetH;
	}

	/* close */

	/**
	 * Closes opened Statements and Connection.
	 * <pre>
	 * PRE:		Connection to database has been established.  Statement has been created.
	 * 			Listings is a table in the database.
	 * POST:	If remove is true, table Listings is dropped, otherwise it is preserved.
	 * 			Open Connection and Statement are closed.
	 * </pre>
	 */
	public void close() {  //.. Close all open connections
		try
		{
//			if ( ! preps.isClosed() ) { preps.close(); }
			if (!stmt.isClosed())
			{
				stmt.close();
			}
			if (!conn.isClosed())
			{
				conn.close();
			}
		} catch (SQLException e)
		{
			System.out.println("\n*** SQLException caught in close() ***\n");
			printSQLException(e);
			e.printStackTrace();
		} catch (NullPointerException npe)
		{
			npe.printStackTrace();
			System.out.println("A NullPointerException was caught in (close).");
		}
	}


	// http://docs.oracle.com/javase/tutorial/jdbc/basics/sqlexception.html
	public static void printSQLException( SQLException ex ) {

		for (Throwable e : ex)
		{
			if (e instanceof SQLException)
			{
				if (!ignoreSQLException(
								((SQLException) e).
												getSQLState()))
				{
					System.err.println("\n****  SQL Exception caught  ****");
					System.err.println("SQLState: " +
									((SQLException) e).getSQLState());

					System.err.println("Error Code: " +
									((SQLException) e).getErrorCode());

					System.err.println("Message: " + e.getMessage());

					Throwable t = ex.getCause();
					while (t != null)
					{
						System.out.println("Cause: " + t);
						t = t.getCause();
					}
					e.printStackTrace(System.err);
				}
			}
		}
	}

	public static boolean ignoreSQLException( String sqlState ) {

		if (sqlState == null)
		{
			System.out.println("The SQL state is not defined!");
			return false;
		}

		// X0Y32: Jar file already exists in schema
		if (sqlState.equalsIgnoreCase("X0Y32"))
		{
			return true;
		}

		// 42Y55: Table already exists in schema
		if (sqlState.equalsIgnoreCase("42Y55"))
		{
			return true;
		}

		return false;
	}

	public static String years( java.util.Date birthDate ) {
		java.util.Date today = new java.util.Date();
		Long diff = (today.getTime() - birthDate.getTime()) / 31536000000L;
		if (diff < 0)
		{
			diff += 100;
		}
//		System.out.println("AGE = Now: "+ today.getTime() +" - Then: "+ birthDate.getTime() +" = "+ (today.getTime() -
// birthDate.getTime()) / (31536000000L) +", diff = "+ diff);
		return diff.toString() + " yrs";
	}
}
