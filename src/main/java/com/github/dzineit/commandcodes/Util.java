package com.github.dzineit.commandcodes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * General utility methods for CommandCodes
 */
public final class Util {
	/**
	 * Converts the given string list into a single string, with each list
	 * element separated by the given separator in the string
	 * 
	 * @param uuidList
	 *            The list of strings to transform into a single string
	 * @param separator
	 *            The string to use in the created string to separate the
	 *            different elements in the list of strings
	 * @return A single string built from the elements in the given string list,
	 *         with elements separated by the given separator
	 */
	public static final String uuidListToString(final List<UUID> uuidList,
			final String separator) {
		final StringBuilder builder = new StringBuilder();
		for (final UUID curUuid : uuidList) {
			builder.append(curUuid.toString()).append(separator);
		}
		// Remove the last separator from the built string
		builder.setLength(builder.length() - separator.length());
		return builder.toString();
	}

	/**
	 * Converts the given string into a UUID list, creating each UUID from each
	 * section of the string split using the given separator
	 * 
	 * @param string
	 *            The string of UUIDs to transform into a list
	 * @param separator
	 *            The string to use to separate the string into different UUIDs
	 *            to be put into a list
	 * @return A list of UUIDs built from the given string split into elements
	 *         using the given separator
	 */
	public static final List<UUID> uuidStringToList(final String string,
			final String separator) {
		final List<UUID> result = new ArrayList<>();
		for (final String cur : string.split(separator)) {
			result.add(UUID.fromString(cur));
		}
		return result;
	}

	private Util() {
	}
}
