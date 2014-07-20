package com.github.dzineit.commandcodes.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.github.dzineit.commandcodes.CommandCodes;
import com.github.dzineit.commandcodes.storage.FileManager;
import com.github.dzineit.commandcodes.storage.JSONFileHandler;
import com.github.dzineit.commandcodes.storage.StorageException;
import com.github.dzineit.commandcodes.storage.org.json.JSONObject;

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
	private Random random = new Random();
	/**
	 * The cap on numbers generated for command codes
	 */
	private int codeCap = 9999;

	/**
	 * Creates a new CodeManager using the given code cap
	 * 
	 * @param codeCap
	 *            The cap on generated numbers for command codes
	 */
	public CodeManager(final CommandCodes plugin) {
		this.plugin = plugin;

		// Get data from config
		codeCap = plugin.getFileManager().getConfig().getInt("code-cap", 9999);

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
		int code;
		do {
			// Continually assign it a new value until its value isn't already taken
			code = random.nextInt(codeCap);
		} while (isInUse(code));

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
	public CommandCode getCurrentCommandCode(final int code) {
		for (final CommandCode cc : currentCodes) {
			if (cc.getCode() == code) {
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
	public CommandCode getSpentCommandCode(final int code) {
		for (final CommandCode cc : oldCodes) {
			if (cc.getCode() == code) {
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
	public CommandCode redeemed(final UUID redeemer, final int code) {
		for (final CommandCode cc : currentCodes) {
			if (cc.getCode() == code) {
				if (!cc.getRedeemers().contains(redeemer)) {
					cc.addRedeemer(redeemer);
					if (cc.isSpent()) {
						currentCodes.remove(cc);
						oldCodes.add(cc);
					}

					return cc;
				}
			}
		}
		return null;
	}

	/**
	 * Checks whether the given code is currently in use as a CommandCode
	 * 
	 * @param code
	 *            The code to check for usage of
	 * @return Whether the given code is currently being used
	 */
	public boolean isInUse(final int code) {
		for (final CommandCode cc : currentCodes) {
			if (cc.getCode() == code) {
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
	 * Gets the current cap for generated command code numbers
	 * 
	 * @return The current command code number cap
	 */
	public int getCodeCap() {
		return codeCap;
	}

	/**
	 * Sets a new cap for generated command code numbers
	 * 
	 * @param codeCap
	 *            The new cap for generated command code numbers
	 */
	public void setCodeCap(final int codeCap) {
		this.codeCap = codeCap;
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
		} catch (StorageException e) {
			file.restoreBackup();
			throw new StorageException(e);
		}

		file.startWriting();
		for (final CommandCode code : currentCodes) {
			file.write(code.toJSONObject());
		}
		file.stopWriting();
	}
}
