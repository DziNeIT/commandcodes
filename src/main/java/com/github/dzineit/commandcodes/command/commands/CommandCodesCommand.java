package com.github.dzineit.commandcodes.command.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.dzineit.commandcodes.CommandCodes;
import com.github.dzineit.commandcodes.command.CCCommandHelper;

public final class CommandCodesCommand implements CommandExecutor {
	private final CCCommandHelper helper;

	public CommandCodesCommand(final CommandCodes plugin) {
		helper = plugin.getCommandHelper();
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd,
			final String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("commandcodes")) {
			if (args.length == 0) {
				helper.sendCommandHelp(sender, null);
			} else {
				final String arg = args[0];
				if (arg.equalsIgnoreCase("info")) {
					helper.sendCommandInfo(sender);
				} else {
					helper.sendCommandHelp(sender, null);
				}
			}
		}

		return true;
	}
}
