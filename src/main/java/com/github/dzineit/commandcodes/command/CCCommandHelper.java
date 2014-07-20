package com.github.dzineit.commandcodes.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.dzineit.commandcodes.CommandCodes;

/**
 * Helper methods for the execution of commands in the CommandCodes Bukkit
 * plugin, such as methods for sending command help and plugin information to a
 * command sender
 */
public final class CCCommandHelper {
	/**
	 * A list of information to send to a command sender asking for help
	 */
	private final List<String> helpInformation;

	public CCCommandHelper(final CommandCodes plugin) {
		helpInformation = new ArrayList<>();
	}

	/**
	 * Registers the given subcommand (with usage parameters) with this helper
	 * along with the description of what the subcommand's functionality is
	 * 
	 * @param subCommand
	 *            The subcommand to register with this helper. This should
	 *            include usage, for example "/lol trees <parameterOne>" where
	 *            <parameterOne> is an argument in the command's syntax
	 * @param description
	 *            A short description of what the subcommand in question
	 *            actually does when it is executed, to be printed to a command
	 *            sender in the event of them requesting help
	 */
	public void registerSubCommand(final String subCommand,
			final String description) {
		helpInformation.add(subCommand + " - " + description);
	}

	/**
	 * Sends help to the given CommandSender object
	 * 
	 * @param sender
	 *            The CommandSender object to send the help to
	 */
	public void sendCommandHelp(final CommandSender sender) {
		for (final String information : helpInformation) {
			sender.sendMessage(ChatColor.GRAY + information);
		}
	}
}
