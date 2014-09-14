package com.bsdarby.controller;

/**
 * Class in voterdata2/com.bsdarby.controller.
 * Created by bsdarby on 9/14/14.
 *
 * This class provides the methods to limit the input characters to those appropriate for the data
 * being entered into a field and to make search easy (trimmed and converted to upper case).
 * By controlling the input, it also minimizes the attack surface, from injection and cross-site
 * scripting attacks.
 *
 * @author Brian Darby
 * @version 1.2
 *          <p/>
 *          TODO - Expand to reads from database, as well as user inputs.
 */

public class SafeChar {

	/* num1 */

	/**
	 * Input validation for numbers. This also allows the use of "*" as a signal
	 * to show all the listings in the database. This is a simple filter that uses
	 * only regex.
	 * <pre>
	 * PRE:  input is not null
	 * POST: If the input contains '*', returns only '*'. Otherwise, return only
	 * the numeric characters contained in the original String, which is trimmed.
	 * </pre>
	 *
	 * @param input ... a String
	 * @return result ... a String containing '*', or the original stripped of
	 * non-numeric characters.
	 */
	public static String num1( String input ) {
		String result = input.replaceAll("[^0-9*\'.%_]", "").trim();
		return result;
	}

	/* num2 */

	/**
	 * Input validation for numbers. This also allows the use of "*" as a signal
	 * to show all the listings in the database. This is a simple filter that uses
	 * only regex.
	 * <pre>
	 * PRE:  input is not null
	 * POST: If the input contains '*', returns only '*'. Otherwise, return only
	 * the numeric characters contained in the original String, which is trimmed.
	 * </pre>
	 *
	 * @param input ... a String
	 * @return result ... a String containing '*', or the original stripped of
	 * non-numeric characters.
	 */
	public static String num2( String input ) {
		String result = input.replaceAll("[^0-9.]", "").trim();
		return result;
	}


	/* text1 */

	/**
	 * Input validation for text. This also allows the use of '*' as a signal to
	 * show all the listings in the database. This is a simple filter that uses
	 * only regex.
	 * <pre>
	 * PRE:  input is not null
	 * POST: If the input contains '*', returns only '*'. Otherwise, return only
	 * the alphanumeric characters contained in the original String, which is
	 * trimmed and converted to all upper case.
	 * </pre>
	 *
	 * @param input ... a String
	 * @return result ... a String containing '*' or the original stripped of
	 * non-alphanumeric characters.
	 * <p/>
	 * TODO - Allow character-wise wildcard. (Br?an)
	 * TODO - Allow non-alphanumerics used in some names (O'Brien, Jean-Marc).
	 */
	public static String text1( String input ) {
		String result = input.replaceAll("[^a-zA-Z0-9 .*?/\'%_-]", "").trim();
		result = result.replaceAll("[%]{2,}", "%");
		result = result.replaceAll("[_%]", "%");
		result = result.replaceAll("[%_]", "%");
		result = result.replace("'", "\'");
		return result;
	}
}
