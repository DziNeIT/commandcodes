package com.github.dzineit.commandcodes;

import java.util.ArrayList;
import java.util.List;

/**
 * General utility methods for CommandCodes
 */
public final class Util {
	/**
	 * Converts the given string list into a single string, with each list
	 * element separated by the given separator in the string
	 * 
	 * @param stringList
	 *            The list of strings to transform into a single string
	 * @param separator
	 *            The string to use in the created string to separate the
	 *            different elements in the list of strings
	 * @return A single string built from the elements in the given string list,
	 *         with elements separated by the given separator
	 */
	public static final String listToString(final List<String> stringList,
			final String separator) {
		final StringBuilder builder = new StringBuilder();
		for (final String curString : stringList) {
			builder.append(curString).append(separator);
		}
		// Remove the last separator from the built string
		builder.setLength(builder.length() - separator.length());
		return builder.toString();
	}

	/**
	 * Converts the given string into a string list, with each section of the
	 * array produced by splitting the string by the given separator counting as
	 * an element in the list
	 * 
	 * @param string
	 *            The string to transform into a list
	 * @param separator
	 *            The string to use to separate the string into different
	 *            elements to be put into a list
	 * @return A list of strings built from the given string split into elements
	 *         using the given separator
	 */
	public static final List<String> stringToList(final String string,
			final String separator) {
		final List<String> result = new ArrayList<>();
		for (final String cur : string.split(separator)) {
			result.add(cur);
		}
		return result;
	}

	private Util() {
	}
}
