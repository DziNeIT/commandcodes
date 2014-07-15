package com.github.dzineit.commandcodes;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.dzineit.commandcodes.code.CodeManager;

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

	@Override
	public void onEnable() {
		File configFile = new File(getDataFolder(), "config.yml");
		File codesFile = new File(getDataFolder(), "codes.yml");

		// Make sure the config file and its directory exist
		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!codesFile.exists()) {
			try {
				codesFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Load yaml configurations
		config = YamlConfiguration.loadConfiguration(configFile);
		codeConfiguration = YamlConfiguration.loadConfiguration(codesFile);

		// Register the plugin's commands
		final CommandCodesCommandExecutor executor = new CommandCodesCommandExecutor(
				this);
		getCommand("ccode").setExecutor(executor);

		codeManager = new CodeManager(config.getInt("code-cap", 9999));
		codeManager.loadCodes(codeConfiguration);
	}

	@Override
	public void onDisable() {
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
}
