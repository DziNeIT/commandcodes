package com.github.dzineit.commandcodes.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.dzineit.commandcodes.CommandCodes;
import com.github.dzineit.commandcodes.code.CodeManager;
import com.github.dzineit.commandcodes.command.CCCommandHelper;

public final class CCodeCommand implements CommandExecutor {
	private final CodeManager codeMgr;
	private final CCCommandHelper helper;

	public CCodeCommand(final CommandCodes plugin) {
		codeMgr = plugin.getCodeManager();
		helper = plugin.getCommandHelper();

		// Register all the /ccode sub commands
		helper.registerSubCommand("generate <command>",
				"Generates a command code for the given command");
		helper.registerSubCommand("remove <code>",
				"Remove the given command code");
		helper.registerSubCommand("redeem <code>",
				"Redeems the given command code, executing the command it is associated with");
		helper.registerSubCommand("view [pageNo]",
				"Views current command codes on the given page");
		helper.registerSubCommand("previous [pageNo]",
				"Views previous command codes on the given page");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd,
			final String label, final String[] args) {
		final String cn = cmd.getName().toLowerCase();

		if (cn.equals("ccode")) {
			if (args.length == 0) {
				helper.sendCommandHelp(sender, args);
			} else {
				final String sub = args[0].toLowerCase();

				if (sub.equals("generate") || sub.equals("create")) {
					if (args.length == 1) {
						sender.sendMessage(ChatColor.DARK_RED
								+ "Invalid syntax, /ccode generate <amount> <command>");
					} else {
						// TODO
					}
				} else if (sub.equals("remove") || sub.equals("delete")) {
					if (args.length == 1) {
						sender.sendMessage(ChatColor.DARK_RED
								+ "Invalid syntax, /ccode remove <code>");
					} else {
						// TODO
					}
				} else if (sub.equals("redeem") || sub.equals("activate")) {
					if (args.length == 1) {
						sender.sendMessage(ChatColor.DARK_RED
								+ "Invalid syntax, /ccode redeem <code>");
					} else {
						// TODO
					}
				} else if (sub.equals("view") || sub.equals("see")) {
					if (args.length > 1) {
						try {
							Integer.parseInt(args[1]);
						} catch (final NumberFormatException e) {
							sender.sendMessage(ChatColor.DARK_RED
									+ "Invalid page number: " + args[1]);
							return true;
						}
					}

					// TODO
				} else if (sub.equals("previous") || sub.equals("seeprev")) {
					if (args.length > 1) {
						try {
							Integer.parseInt(args[1]);
						} catch (final NumberFormatException e) {
							sender.sendMessage(ChatColor.DARK_RED
									+ "Invalid page number: " + args[1]);
							return true;
						}
					}

					// TODO
				}
			}
		}

		return true;
	}
}
