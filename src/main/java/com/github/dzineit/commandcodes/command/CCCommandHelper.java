package com.github.dzineit.commandcodes.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.dzineit.commandcodes.CommandCodes;

public final class CCCommandHelper {
	private static final int COMMANDS_PER_PAGE = 7;

	private final List<String> subs;

	public CCCommandHelper(final CommandCodes plugin) {
		subs = new ArrayList<>();
	}

	public void registerSubCommand(final String subCommand,
			final String description) {
		subs.add(subCommand + " - " + description);
	}

	public void sendCommandHelp(final CommandSender sender, final String[] args) {
		final int numCommands = subs.size();
		// Work out the number of pages we have
		final int pages = (int) Math.ceil(numCommands / COMMANDS_PER_PAGE);

		int pageNo = 1;

		// Get the page number from the command arguments
		if (args != null && args.length > 0) {
			try {
				pageNo = Integer.parseInt(args[0]);
				if (pageNo > pages || pageNo < 0) {
					sender.sendMessage(ChatColor.DARK_RED
							+ "Invalid page number: " + args[0]);
				}
			} catch (final NumberFormatException e) {
				sender.sendMessage(ChatColor.DARK_RED + "Invalid page number: "
						+ args[0]);
			}
		}

		if (pages > 1) {
			sender.sendMessage(ChatColor.GRAY
					+ "== CommandCodes Commands - Page " + pageNo + "/" + pages
					+ " ==");
		} else {
			sender.sendMessage(ChatColor.GRAY + "== CommandCodes Commands ==");
		}

		final int startPoint = COMMANDS_PER_PAGE * (pageNo - 1);
		for (int cur = startPoint; cur < startPoint + 7; cur++) {
			sender.sendMessage(ChatColor.GRAY + "/ccode " + subs.get(cur));
		}
	}

	public void sendCommandInfo(final CommandSender sender) {
		// TODO
	}
}
