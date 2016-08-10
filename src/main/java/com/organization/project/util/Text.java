package com.organization.project.util;

import java.util.Random;

public class Text {

	private static final String SALT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

	public static String getSaltString(int length) {
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < length) {
			int index = (int) (rnd.nextFloat() * SALT_CHARS.length());
			salt.append(SALT_CHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}
}
