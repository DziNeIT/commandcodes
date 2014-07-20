package com.github.dzineit.commandcodes.command.ccode;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.dzineit.commandcodes.CommandCodes;
import com.github.dzineit.commandcodes.code.CodeManager;
import com.github.dzineit.commandcodes.code.CommandCode;
import com.github.dzineit.commandcodes.command.CCodeSubCommand;

public final class CCodeRedeemCommand extends CCodeSubCommand {
	/**
	 * The plugin's code manager
	 */
	private final CodeManager codeMgr;

	public CCodeRedeemCommand(final CommandCodes plugin) {
		super(plugin);

		codeMgr = plugin.getCodeManager();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(final CommandSender sender, final String[] args) {
		if (!sender.hasPermission("commandcodes.redeem")) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "You don't have permission to do that!");
		} else {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.DARK_RED
						+ "Only players can redeem command codes!");
			} else {
				if (args.length == 1) {
					sender.sendMessage(ChatColor.DARK_RED
							+ "Invalid syntax, /ccode redeem <code>");
				} else {
					int code;
					try {
						code = Integer.parseInt(args[1]);
					} catch (final NumberFormatException e) {
						code = 10000000;
						sender.sendMessage(ChatColor.DARK_RED
								+ "Invalid code entered, /ccode redeem <code>");
					}

					final Player player = (Player) sender;

					if (code != 10000000) {
						// Redeems the code with the CodeManager
						final CommandCode cc = codeMgr.redeemCode(
								player.getUniqueId(), code);

						if (cc == null) { // If they have already redeemed it
							sender.sendMessage(ChatColor.DARK_RED
									+ "Couldn't redeem command code!");
						} else {
							sender.sendMessage(ChatColor.GRAY
									+ "Redeemed code!");
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
		return new String[] { "redeem", "activate" };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUsage() {
		return "/ccode redeem <code>";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return "Redeems and activates the given code";
	}
}
