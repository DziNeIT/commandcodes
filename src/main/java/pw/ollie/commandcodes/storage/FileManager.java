package pw.ollie.commandcodes.storage;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import pw.ollie.commandcodes.CommandCodes;

/**
 * Manages all of the files involved in the CommandCodes Bukkit plugin,
 * including the configuration as well as the storage for command codes
 */
public final class FileManager {
	private final CommandCodes plugin;

	// Files
	private final File configFile;
	private final File codeStorage;

	/**
	 * The yaml configuration for the plugin
	 */
	private final YamlConfiguration config;
	/**
	 * The JSON file handler for storing current command codes
	 */
	private final JSONFileHandler codeStore;

	public FileManager(final CommandCodes plugin) {
		this.plugin = plugin;

		final File dataFolder = plugin.getDataFolder();

		// Create file objects
		configFile = getFile("config.yml");
		codeStorage = getFile("curcodes.json");

		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
		if (!configFile.exists()) {
			plugin.saveResource("config.yml", false);
		}
		if (!codeStorage.exists()) {
			try {
				codeStorage.createNewFile();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		// Create configuration / data objects
		config = YamlConfiguration.loadConfiguration(configFile);
		codeStore = new JSONFileHandler(codeStorage);
	}

	public File getConfigFile() {
		return configFile;
	}

	public File getCodeStorageFile() {
		return codeStorage;
	}

	public YamlConfiguration getConfig() {
		return config;
	}

	/**
	 * Gets the JSONFileHandler which handles the storage of current codes
	 * 
	 * @return The JSONFileHandler for the file which stores current command
	 *         codes
	 */
	public JSONFileHandler getCodeStore() {
		return codeStore;
	}

	/**
	 * Gets the file with the given name within the CommandCodes plugin's data
	 * folder
	 * 
	 * @param name
	 *            The name of the file within the CommandCodes plugin's data
	 *            folder to get
	 * @return A File object for the file in the plugin data folder with the
	 *         given name
	 */
	public File getFile(final String name) {
		return new File(plugin.getDataFolder(), name);
	}
}
