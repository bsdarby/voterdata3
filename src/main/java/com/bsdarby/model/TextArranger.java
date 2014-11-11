package com.bsdarby.model;

/**
 * Class in voterdata2/com.bsdarby.model.
 * Created by bsdarby on 11/9/14.
 */
public class TextArranger {

	public static String makeEllipsis(String text, int maxLength ){

		String resultingText;

		if(maxLength < text.length()) {
			resultingText = "..." + text.substring(text.length() - maxLength - 3);
		} else {
			resultingText  = text;
		}
		return resultingText;
	}


}
