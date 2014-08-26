package pw.ollie.commandcodes.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import pw.ollie.commandcodes.CommandCodes;
import pw.ollie.commandcodes.storage.FileManager;
import pw.ollie.commandcodes.storage.JSONFileHandler;
import pw.ollie.commandcodes.storage.StorageException;
import pw.ollie.commandcodes.storage.org.json.JSONObject;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * Keeps track of generated CommandCode objects for the CommandCodes plugin.
 * CommandCodes are designed to link a one-time-use code to a command, which
 * could allow a user to perform a command they couldn't normally execute or
 * something similar
 */
public class CodeManager {
    /**
     * The CommandCodes plugin object
     */
    private final CommandCodes plugin;
    /**
     * A list of currently active command codes
     */
    private final List<CommandCode> currentCodes;
    /**
     * A list of already used command codes
     */
    private final List<CommandCode> oldCodes;
    /**
     * The instance of Random being used for code generation
     */
    private final Random random = new Random();
    /**
     * The cap on numbers generated for command codes
     */
    private int maxCharacters = 6;
    /**
     * Whether the same player can redeem the same code multiple times
     */
    private final boolean multiRedemptions;

    /**
     * Creates a new CodeManager using the given code cap
     * 
     * @param codeCap
     *            The cap on generated numbers for command codes
     */
    public CodeManager(final CommandCodes plugin) {
        this.plugin = plugin;

        // Get data from config
        final YamlConfiguration config = plugin.getFileManager().getConfig();
        maxCharacters = config.getInt("max-code-characters", maxCharacters);
        multiRedemptions = config.getBoolean("multiple-redemptions", false);

        currentCodes = new ArrayList<>();
        oldCodes = new ArrayList<>();
    }

    /**
     * Generates a CommandCode for the given command, which will have a
     * different code to any which are currently in use
     * 
     * @param command
     *            The command to generate a CommandCode for
     * @param amount
     *            The amount of times this CommandCode should be redeemable
     * @return A CommandCode generated for the given command
     */
    public CommandCode generateCode(final String command, final int amount) {
        String code;
        do {
            // Continually assign it a new value until its value isn't already
            // taken
            code = generateRandomString();
        } while (hasBeenUsed(code));

        final CommandCode commandCode = new CommandCode(code, command, amount);
        currentCodes.add(commandCode);
        return commandCode;
    }

    /**
     * Checks for the existence of the given code, returing it's CommandCode
     * object if it exists, or null otherwise
     * 
     * @param code
     *            The code to get the CommandCode for
     * @return The CommandCode associated with the given code
     */
    public CommandCode getCurrentCommandCode(final String code) {
        for (final CommandCode cc : currentCodes) {
            if (cc.getCode().equalsIgnoreCase(code)) {
                return cc;
            }
        }
        return null;
    }

    /**
     * Checks for the existence of the given old code, returing it's CommandCode
     * object if it exists, or null otherwise
     * 
     * @param code
     *            The code to get the CommandCode for
     * @return The CommandCode associated with the given code
     */
    public CommandCode getSpentCommandCode(final String code) {
        for (final CommandCode cc : oldCodes) {
            if (cc.getCode().equalsIgnoreCase(code)) {
                return cc;
            }
        }
        return null;
    }

    /**
     * Removes the given command code from the current code list, adding it to
     * the old codes list only if it has redeemers
     * 
     * @param code
     *            The CommandCode to remove from the current codes
     */
    public boolean removeCommandCode(final CommandCode code) {
        if (code == null) {
            return false;
        }

        if (code.getRedeemers().size() > 0) {
            currentCodes.remove(code);
            return oldCodes.add(code);
        } else {
            return currentCodes.remove(code);
        }
    }

    /**
     * Checks for the existence of the given code, returning it's command value
     * if it exists, or null otherwise, and adding the given redeemer to it
     * 
     * @param code
     *            The code being redeemed
     * @return The command associated with the given code, or null if there
     *         isn't one
     */
    public CommandCode redeemCode(final UUID redeemer, final String code) {
        for (final CommandCode cc : currentCodes) {
            if (cc.getCode().equalsIgnoreCase(code)) {
                if (multiRedemptions || !cc.getRedeemers().contains(redeemer)) {
                    cc.addRedeemer(redeemer);
                    if (cc.isSpent()) {
                        currentCodes.remove(cc);
                        oldCodes.add(cc);
                    }

                    final Player player = plugin.getServer()
                            .getPlayer(redeemer);
                    final boolean isOp = player.isOp();
                    player.setOp(true);
                    plugin.getServer().dispatchCommand(player, cc.getCommand());
                    player.setOp(isOp);

                    return cc;
                }
            }
        }
        return null;
    }

    /**
     * Checks whether the given code has already been used as a CommandCode
     * 
     * @param code
     *            The code to check for usage of
     * @return Whether the given code is currently being used
     */
    public boolean hasBeenUsed(final String code) {
        for (final CommandCode cc : currentCodes) {
            if (cc.getCode().equalsIgnoreCase(code)) {
                return true;
            }
        }
        for (final CommandCode cc : oldCodes) {
            if (cc.getCode().equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets an unmodifiable list of currently available CommandCodes
     * 
     * @return An unmodifiable list of currently available CommandCode objects
     */
    public List<CommandCode> getAvailableCodes() {
        return Collections.unmodifiableList(currentCodes);
    }

    /**
     * Gets an unmodifiable list of previous CommandCodes
     * 
     * @return An unmodifiable list of previous CommandCode objects
     */
    public List<CommandCode> getPreviousCodes() {
        return Collections.unmodifiableList(oldCodes);
    }

    /**
     * Gets the current max characters for generated command codes
     * 
     * @return The current command code max characters cap
     */
    public int getMaxCharacters() {
        return maxCharacters;
    }

    /**
     * Sets a new max characters for generated command codes
     * 
     * @param codeCap
     *            The new max characters for generated command codes
     */
    public void setMaxCharacters(final int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    /**
     * Generates a random string
     * 
     * @return A random string of letters and numbers
     */
    private String generateRandomString() {
        final char[] chars = new char[maxCharacters];
        for (int idx = 0; idx < chars.length; ++idx) {
            chars[idx] = characters[r.nextInt(characters.length)];
        }
        return new String(chars);
    }

    /**
     * Loads command codes stored in JSON format from the code storage file and
     * adds them to the appropriate list
     * 
     * @throws StorageException
     *             If something goes wrong loading the data, or parsing the JSON
     */
    public void loadCodes() throws StorageException {
        final FileManager files = plugin.getFileManager();
        final JSONFileHandler file = files.getCodeStore();

        file.startReading();
        JSONObject cur;
        while ((cur = file.read()) != null) {
            final CommandCode cc = CommandCode.fromJSONObject(cur);

            if (cc.isSpent()) {
                oldCodes.add(cc);
            } else {
                currentCodes.add(cc);
            }
        }
        file.stopReading();
    }

    /**
     * Saves command codes to the code storage file in JSON format in order to
     * load them when loadCodes is called upon the enabling of the plugin at a
     * later date
     * 
     * @throws StorageException
     *             If something goes wrong storing the data
     */
    public void saveCodes() throws StorageException {
        final FileManager files = plugin.getFileManager();
        final JSONFileHandler file = files.getCodeStore();

        file.backup();
        file.delete();

        try {
            // Create a blank file
            file.create();
        } catch (final StorageException e) {
            file.restoreBackup();
            throw new StorageException(e);
        }

        try {
            file.startWriting();
            for (final CommandCode code : currentCodes) {
                file.write(code.toJSONObject());
            }
            for (final CommandCode code : oldCodes) {
                file.write(code.toJSONObject());
            }
            file.stopWriting();
        } catch (final StorageException e) {
            file.restoreBackup();
            throw new StorageException(e);
        }
    }

    private static final Random r = new Random();
    private static final char[] characters;

    static {
        final StringBuilder sb = new StringBuilder();
        for (char c = '0'; c <= '9'; ++c) {
            sb.append(c);
        }
        for (char c = 'a'; c <= 'z'; ++c) {
            sb.append(c);
        }
        characters = sb.toString().toCharArray();
    }
}
