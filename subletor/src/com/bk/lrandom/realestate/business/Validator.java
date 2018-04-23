package com.bk.lrandom.realestate.business;

import java.util.regex.Pattern;

public class Validator {
	public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	public static final Pattern USER_NAME_PATTERN = Pattern
			.compile("^[a-z0-9_-]{5,50}$");

	public static boolean validEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	public static boolean validUserName(String userName) {
		return USER_NAME_PATTERN.matcher(userName).matches();
	}
}
