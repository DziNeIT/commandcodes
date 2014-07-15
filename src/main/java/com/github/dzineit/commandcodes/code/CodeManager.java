package com.github.dzineit.commandcodes.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Keeps track of generated CommandCode objects for the CommandCodes plugin.
 * CommandCodes are designed to link a one-time-use code to a command, which
 * could allow a user to perform a command they couldn't normally execute or
 * something similar
 */
public class CodeManager {
	/**
	 * A list of currently active command codes
	 */
	private List<CommandCode> currentCodes;
	/**
	 * A list of already used command codes
	 */
	private List<CommandCode> oldCodes;
	/**
	 * The instance of Random being used for code generation
	 */
	private Random random;
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
	public CodeManager(final int codeCap) {
		this.codeCap = codeCap;

		currentCodes = new ArrayList<>();
		oldCodes = new ArrayList<>();
	}

	/**
	 * Loads stored command codes from a file
	 * 
	 * TODO: Create a more efficient storage mechanism (than YAML)
	 */
	public void loadCodes(YamlConfiguration conf) {
		ConfigurationSection sec = conf.getConfigurationSection("current");
		if (sec == null) {
			sec = conf.createSection("current");
			sec.set("null", "null");
		}
		Set<String> keys = sec.getKeys(false);

		for (final String key : keys) {
			try {
				if (sec.getString(key).equals("__________")) {
					continue;
				}

				currentCodes.add(new CommandCode(Integer.parseInt(key), sec
						.getString(key)));
			} catch (NumberFormatException e) {
				continue;
			}
		}

		sec = conf.getConfigurationSection("previous");
		if (sec == null) {
			sec = conf.createSection("previous");
			sec.set("null", "null");
		}
		keys = sec.getKeys(false);

		for (final String key : keys) {
			try {
				final int code = Integer.parseInt(key);
				final String[] split = sec.getString(key).split("::");

				oldCodes.add(new CommandCode(code, split[0], split[1]));
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException nobodyCares) {
				continue;
			}
		}
	}

	/**
	 * Saves command codes to a file in order to load them when loadCodes is
	 * called
	 * 
	 * TODO: Create a more efficient storage mechanism (than YAML)
	 */
	public void saveCodes(YamlConfiguration conf) {
		ConfigurationSection sec = conf.getConfigurationSection("current");
		if (sec == null) {
			sec = conf.createSection("current");
		}

		final List<String> codes = new ArrayList<>();
		for (final CommandCode cc : currentCodes) {
			codes.add(String.valueOf(cc.getCode()));

			sec.set(String.valueOf(cc.getCode()), cc.getCommand());
		}

		for (final String key : sec.getKeys(false)) {
			if (!codes.contains(key)) {
				sec.set(key, "__________");
			}
		}

		sec = conf.getConfigurationSection("previous");
		if (sec == null) {
			sec = conf.createSection("previous");
		}

		for (final CommandCode cc : oldCodes) {
			sec.set(String.valueOf(cc.getCode()), cc.getCommand());
		}
	}

	/**
	 * Generates a CommandCode for the given command, which will have a
	 * different code to any which are currently in use
	 * 
	 * @param command
	 *            The command to generate a CommandCode for
	 * @return A CommandCode generated for the given command
	 */
	public CommandCode generateCode(String command) {
		int code;
		do {
			// Continually assign it a new value until its value isn't already taken
			code = random.nextInt(codeCap);
		} while (isInUse(code));

		CommandCode cc = new CommandCode(code, command);
		currentCodes.add(cc);
		return cc;
	}

	/**
	 * Checks for the existence of the given code, returing it's CommandCode
	 * object if it exists, or null otherwise
	 * 
	 * @param code
	 *            The code to get the CommandCode for
	 * @return The CommandCode associated with the given code
	 */
	public CommandCode getCommandCode(int code) {
		for (CommandCode cc : currentCodes) {
			if (cc.getCode() == code) {
				return cc;
			}
		}
		return null;
	}

	/**
	 * Checks for the existence of the given code, returning it's command value
	 * if it exists, or null otherwise. WARNING: This removes the CommandCode
	 * from the list of current codes if it is found, so it should only be used
	 * when the code is actually redeemed. Checks should be performed using
	 * getCommandCode(int code) instead
	 * 
	 * @param code
	 *            The code being redeemed
	 * @return The command associated with the given code, or null if there
	 *         isn't one
	 */
	public String redeemed(int code) {
		for (CommandCode cc : currentCodes) {
			if (cc.getCode() == code) {
				currentCodes.remove(cc);
				oldCodes.add(cc);
				return cc.getCommand();
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
	public boolean isInUse(int code) {
		for (CommandCode cc : currentCodes) {
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
	public void setCodeCap(int codeCap) {
		this.codeCap = codeCap;
	}
}
