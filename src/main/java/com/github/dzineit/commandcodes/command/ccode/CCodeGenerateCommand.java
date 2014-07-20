package com.github.dzineit.commandcodes.command.ccode;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.dzineit.commandcodes.CommandCodes;
import com.github.dzineit.commandcodes.code.CodeManager;
import com.github.dzineit.commandcodes.code.CommandCode;
import com.github.dzineit.commandcodes.command.CCodeSubCommand;

/**
 * The subcommand of the 'ccode' command which deals with generation of new
 * command codes by an admin
 */
public final class CCodeGenerateCommand extends CCodeSubCommand {
	/**
	 * The plugin's code manager
	 */
	private final CodeManager codeMgr;

	public CCodeGenerateCommand(final CommandCodes plugin) {
		super(plugin);

		codeMgr = plugin.getCodeManager();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(final CommandSender sender, final String[] args) {
		if (args.length < 3) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "Invalid syntax, /ccode generate <amount> <command>");
		} else {
			boolean ia = false; // Invalid amount
			int amount = -1;

			try {
				amount = Integer.parseInt(args[1]);
			} catch (final NumberFormatException e) {
				ia = true;
			}

			if (amount < 1) {
				ia = true;
			}

			if (ia) {
				sender.sendMessage(ChatColor.DARK_RED
						+ "Invalid amount entered, /ccode generate <amount> <command>");
			} else {
				final StringBuilder builder = new StringBuilder();
				for (int cur = 2; cur < args.length; cur++) {
					builder.append(args[cur]).append(" ");
				}

				final CommandCode code = codeMgr.generateCode(
						builder.toString(), amount);

				if (code != null) {
					sender.sendMessage(ChatColor.GRAY
							+ "Successfully created command code which can be used "
							+ code.getAmount()
							+ " times, linked to the command: '"
							+ code.getCommand() + "'");
				} else {
					sender.sendMessage(ChatColor.DARK_RED
							+ "Failed to create code!");
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getNames() {
		return new String[] { "generate", "create" };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUsage() {
		return "/ccode generate <amount> <command>";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return "Generates a code for the given command";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPermission() {
		return "commandcodes.generate";
	}
}
