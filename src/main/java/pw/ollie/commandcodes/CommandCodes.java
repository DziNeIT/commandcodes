package pw.ollie.commandcodes;

import org.bukkit.plugin.java.JavaPlugin;

import pw.ollie.commandcodes.code.CodeManager;
import pw.ollie.commandcodes.command.CCodeCommand;
import pw.ollie.commandcodes.storage.FileManager;
import pw.ollie.commandcodes.storage.StorageException;

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
	 * The base command object, containing helpful methods for command parsing
	 * as well as acting as a base executor for the 'ccode' command
	 */
	private CCodeCommand baseCommand;
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
		} catch (final StorageException e) {
			e.printStackTrace();
		}

		// Register the plugin's commands
		baseCommand = new CCodeCommand(this);
		baseCommand.createSubCommands();
		getCommand("ccode").setExecutor(baseCommand);
	}

	@Override
	public void onDisable() {
		// Save the command codes to a JSON file
		try {
			codeManager.saveCodes();
		} catch (final StorageException e) {
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
	 * Gets the CCodeCommand object for the plugin, which contains helpful
	 * utility methods for the handling of commands for CommandCodes as well as
	 * acting as a base executor for the 'ccode' command
	 * 
	 * @return The plugin's CCodeCommand object
	 */
	public CCodeCommand getBaseCommand() {
		return baseCommand;
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
