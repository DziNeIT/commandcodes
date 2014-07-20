package com.github.dzineit.commandcodes.command.ccode;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.github.dzineit.commandcodes.CommandCodes;
import com.github.dzineit.commandcodes.code.CodeManager;
import com.github.dzineit.commandcodes.command.CCodeSubCommand;
import com.github.dzineit.commandcodes.util.CommandUtil;

public final class CCodeViewCommand extends CCodeSubCommand {
	/**
	 * The plugin's code manager
	 */
	private final CodeManager codeMgr;

	public CCodeViewCommand(final CommandCodes plugin) {
		super(plugin);

		codeMgr = plugin.getCodeManager();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(final CommandSender sender, final String[] args) {
		if (!(sender.hasPermission("commandcodes.view") || sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "You don't have permission to do that!");
		} else {
			CommandUtil.displayCodeList(sender, codeMgr.getAvailableCodes(),
					args);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getNames() {
		return new String[] { "view", "see" };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUsage() {
		return "/ccode view [page]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return "Shows a list of current codes at the given page";
	}
}
