package com.github.dzineit.commandcodes;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.dzineit.commandcodes.code.CodeManager;
import com.github.dzineit.commandcodes.command.CCCommandHelper;
import com.github.dzineit.commandcodes.command.commands.CCodeCommand;
import com.github.dzineit.commandcodes.command.commands.CommandCodesCommand;
import com.github.dzineit.commandcodes.storage.FileManager;
import com.github.dzineit.commandcodes.storage.StorageException;

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
	 * The CommandHelper object, containing helpful methods for command parsing
	 */
	private CCCommandHelper cmdHelper;
	/**
	 * The FileManager object, which manages all of the files for the plugin
	 */
	private FileManager fileManager;

	@Override
	public void onEnable() {
		// Load files / config
		fileManager = new FileManager(this);

		// Load the command manager and the saved command codes
		codeManager = new CodeManager(this);

		try {
			codeManager.loadCodes();
		} catch (StorageException e) {
			e.printStackTrace();
		}

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
		try {
			codeManager.saveCodes();
		} catch (StorageException e) {
			e.printStackTrace();
		}
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

	/**
	 * Gets the FileManager object for the plugin, which handles all management
	 * of files in CommandCodes, such as the configuration file as well as other
	 * files
	 * 
	 * @return The plugin's FileManager object
	 */
	public FileManager getFileManager() {
		return fileManager;
	}
}
