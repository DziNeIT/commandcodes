package com.github.dzineit.commandcodes.command.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.dzineit.commandcodes.CommandCodes;
import com.github.dzineit.commandcodes.command.CCCommandHelper;

/**
 * The CommandExecutor implementation which handles the CommandCodes command
 * 'commandcodes'. This command's functions are related to information and help
 * about and for the plugin
 */
public final class CommandCodesCommand implements CommandExecutor {
	/**
	 * The command helper used for helper methods while executing the
	 * 'commandcodes' command
	 */
	private final CCCommandHelper helper;

	/**
	 * Creates a new command handler for the 'commandcodes' command
	 * 
	 * @param plugin
	 *            The plugin this command executor is acting for
	 */
	public CommandCodesCommand(final CommandCodes plugin) {
		helper = plugin.getCommandHelper();
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd,
			final String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("commandcodes")) {
			if (args.length == 0) {
				helper.sendCommandHelp(sender);
			} else {
				final String arg = args[0];
				if (arg.equalsIgnoreCase("info")) {
					helper.sendPluginInfo(sender);
				} else {
					helper.sendCommandHelp(sender);
				}
			}
		}

		return true;
	}
}
