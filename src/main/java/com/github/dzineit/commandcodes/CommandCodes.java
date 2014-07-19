package com.github.dzineit.commandcodes;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.dzineit.commandcodes.code.CodeManager;
import com.github.dzineit.commandcodes.command.CCCommandHelper;
import com.github.dzineit.commandcodes.command.commands.CCodeCommand;
import com.github.dzineit.commandcodes.command.commands.CommandCodesCommand;

/**
 * The main class for the CommandCodes plugin for Bukkit, which allows the
 * binding of a code to a command in order to allow users to execute commands
 * via codes, even if they wouldn't normally be permitted to execute the given
 * command
 */
public final class CommandCodes extends JavaPlugin {
	/**
	 * The CodeManager for the plugin, which keeps track of all of the current
	 * and old CommandCode objects
	 */
	private CodeManager codeManager;
	/**
	 * The yaml configuration for the plugin
	 */
	private YamlConfiguration config;
	/**
	 * The yaml configuration for storing command codes
	 */
	private YamlConfiguration codeConfiguration;
	/**
	 * The CommandHelper object, containing helpful methods for command parsing
	 */
	private CCCommandHelper cmdHelper;

	@Override
	public void onEnable() {
		// Create the objects for the config and codes storage files
		final File configFile = new File(getDataFolder(), "config.yml");
		final File codesFile = new File(getDataFolder(), "codes.yml");

		// Make sure all files and directories exist
		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		if (!codesFile.exists()) {
			try {
				codesFile.createNewFile();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		// Load yaml configurations
		config = YamlConfiguration.loadConfiguration(configFile);
		codeConfiguration = YamlConfiguration.loadConfiguration(codesFile);

		// Load the command manager and the saved command codes
		codeManager = new CodeManager(config.getInt("code-cap", 9999));
		codeManager.loadCodes(codeConfiguration);

		// Create the command helper
		cmdHelper = new CCCommandHelper(this);

		// Register the plugin's commands
		final CCodeCommand execMain = new CCodeCommand(this);
		final CommandCodesCommand execInfo = new CommandCodesCommand(this);

		getCommand("ccode").setExecutor(execMain);
		getCommand("commandcodes").setExecutor(execInfo);
	}

	@Override
	public void onDisable() {
		// Save the command codes to a YML file
		codeManager.saveCodes(codeConfiguration);
	}

	/**
	 * Gets the CodeManager object for the plugin, which keeps track of
	 * CommandCode objects and functions as a form of API for other plugins
	 * which want to use the CommandCodes system
	 * 
	 * @return The plugin's CodeManager object
	 */
	public CodeManager getCodeManager() {
		return codeManager;
	}

	/**
	 * Gets the CommandHelper object for the plugin, which contains helpful
	 * utility methods for the handling of commands for CommandCodes
	 * 
	 * @return The plugin's CommandHelper object
	 */
	public CCCommandHelper getCommandHelper() {
		return cmdHelper;
	}
}
