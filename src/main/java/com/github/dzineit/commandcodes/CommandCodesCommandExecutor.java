package com.github.dzineit.commandcodes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class CommandCodesCommandExecutor implements CommandExecutor {
	private final CommandCodes plugin;

	public CommandCodesCommandExecutor(final CommandCodes plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd,
			final String label, final String[] args) {
		final String cn = cmd.getName().toLowerCase();

		if (cn.equals("ccode")) {
			// TODO
		}

		return true;
	}
}
