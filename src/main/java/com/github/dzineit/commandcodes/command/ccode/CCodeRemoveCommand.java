package com.github.dzineit.commandcodes.command.ccode;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.github.dzineit.commandcodes.CommandCodes;
import com.github.dzineit.commandcodes.code.CodeManager;
import com.github.dzineit.commandcodes.code.CommandCode;
import com.github.dzineit.commandcodes.command.CCodeSubCommand;

public final class CCodeRemoveCommand extends CCodeSubCommand {
	/**
	 * The plugin's code manager
	 */
	private final CodeManager codeMgr;

	public CCodeRemoveCommand(final CommandCodes plugin) {
		super(plugin);

		codeMgr = plugin.getCodeManager();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(final CommandSender sender, final String[] args) {
		if (!(sender.hasPermission("commandcodes.remove") || sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "You don't have permission to do that!");
		} else {
			if (args.length == 1) {
				sender.sendMessage(ChatColor.DARK_RED
						+ "Invalid syntax, /ccode remove <code>");
			} else {
				int code;
				try {
					code = Integer.parseInt(args[1]);
				} catch (final NumberFormatException e) {
					code = 10000000;
					sender.sendMessage(ChatColor.DARK_RED
							+ "Invalid code entered, /ccode remove <code>");
				}

				if (code != 10000000) {
					final CommandCode cc = codeMgr.getCurrentCommandCode(code);

					if (cc == null) {
						sender.sendMessage(ChatColor.DARK_RED
								+ "A command code with that code doesn't exist!");
					} else {
						if (codeMgr.removeCommandCode(cc)) {
							sender.sendMessage(ChatColor.GRAY
									+ "Successfully removed command code!");
						} else {
							sender.sendMessage(ChatColor.GRAY
									+ "Couldn't remove command code!");
						}
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getNames() {
		return new String[] { "remove", "delete" };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUsage() {
		return "/ccode remove <code>";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return "Removes the given code";
	}
}
