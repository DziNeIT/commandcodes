package pw.ollie.commandcodes.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import pw.ollie.commandcodes.CommandCodes;
import pw.ollie.commandcodes.command.ccode.CCodeGenerateCommand;
import pw.ollie.commandcodes.command.ccode.CCodePreviousCommand;
import pw.ollie.commandcodes.command.ccode.CCodeRedeemCommand;
import pw.ollie.commandcodes.command.ccode.CCodeRemoveCommand;
import pw.ollie.commandcodes.command.ccode.CCodeShowCommand;
import pw.ollie.commandcodes.command.ccode.CCodeViewCommand;

/**
 * The base command executor of the 'ccode' command, which redirects subcommands
 * to the appropriate executor, if one has been registered
 */
public final class CCodeCommand implements CommandExecutor {
	/**
	 * The CommandCodes plugin object this command is registered to
	 */
	private final CommandCodes plugin;
	/**
	 * A map of subcommands of the 'ccode' command
	 */
	private final Map<String, CCodeSubCommand> subCommands = new HashMap<>();

	public CCodeCommand(final CommandCodes plugin) {
		this.plugin = plugin;
	}

	/**
	 * Registers the given subcommand and all of it's aliases to this command
	 * 
	 * @param subCommand
	 *            The subcommand to register
	 */
	public void registerSubCommand(final CCodeSubCommand subCommand) {
		for (final String name : subCommand.getNames()) {
			subCommands.put(name.toLowerCase(), subCommand);
		}
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd,
			final String label, final String[] args) {
		if (args.length == 0) {
			sendCommandHelp(sender);
		} else {
			final CCodeSubCommand command = subCommands.get(args[0]);

			if (command != null) {
				if (sender.hasPermission(command.getPermission())
						|| sender instanceof ConsoleCommandSender) {
					command.execute(sender, args);
				} else {
					sender.sendMessage(ChatColor.DARK_RED
							+ "You don't have permission to do that!");
				}
			} else {
				sender.sendMessage(ChatColor.DARK_RED
						+ "That subcommand of /ccode doesn't exist! /ccode for help!");
			}
		}

		return true;
	}

	private final List<String> stringList = new ArrayList<>();

	/**
	 * Sends help to the given command sender, in the form of a list of commands
	 * and a description of what each one does
	 * 
	 * @param sender
	 *            The CommandSender object to send the help messages to
	 */
	public void sendCommandHelp(final CommandSender sender) {
		stringList.clear();

		for (final CCodeSubCommand subCommand : subCommands.values()) {
			if (stringList.contains(subCommand.getUsage())) {
				continue;
			}
			if (!sender.hasPermission(subCommand.getPermission())) {
				continue;
			}

			sender.sendMessage(ChatColor.GRAY + subCommand.getUsage() + " - "
					+ subCommand.getDescription());
			stringList.add(subCommand.getUsage());
		}

		stringList.clear();
	}

	/**
	 * Creates all of the subcommands for the 'ccode' command. They are then
	 * automatically registered in the CCodeSubCommand constructor
	 */
	public void createSubCommands() {
		new CCodeGenerateCommand(plugin);
		new CCodePreviousCommand(plugin);
		new CCodeRedeemCommand(plugin);
		new CCodeRemoveCommand(plugin);
		new CCodeShowCommand(plugin);
		new CCodeViewCommand(plugin);
	}
}
