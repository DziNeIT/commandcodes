package pw.ollie.commandcodes.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * General utility methods for CommandCodes
 */
public final class GeneralUtil {
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
        if (builder.length() > 0) {
            // Remove the last separator from the built string
            builder.setLength(builder.length() - separator.length());
        }
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
        if (!string.contains(separator) && string.length() != 0) {
            result.add(UUID.fromString(string));
            return result;
        }
        final String[] split = string.split(separator);
        if (split == null || split.length == 0) {
            return result;
        }
        for (final String cur : split) {
            result.add(UUID.fromString(cur));
        }
        return result;
    }

    private GeneralUtil() {
    }
}
