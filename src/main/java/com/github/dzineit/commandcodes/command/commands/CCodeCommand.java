package com.github.dzineit.commandcodes.command.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.github.dzineit.commandcodes.CommandCodes;
import com.github.dzineit.commandcodes.code.CodeManager;
import com.github.dzineit.commandcodes.code.CommandCode;
import com.github.dzineit.commandcodes.command.CCCommandHelper;

/**
 * The CommandExecutor implementation which handles the CommandCodes command
 * 'ccode'. This command's functions are related to creating, removing,
 * redeeming and viewing (present and past, in the case of viewing) command
 * codes
 */
public final class CCodeCommand implements CommandExecutor {
	/**
	 * The CommandCodes plugin instance
	 */
	private final CommandCodes plugin;
	/**
	 * The plugin code manager
	 */
	private final CodeManager codeMgr;
	/**
	 * The plugin command helper
	 */
	private final CCCommandHelper helper;

	/**
	 * Creates a new command handler for CommandCodes' 'ccode' command
	 * 
	 * @param plugin
	 *            The CommandCodes plugin object this command executor is acting
	 *            for
	 */
	public CCodeCommand(final CommandCodes plugin) {
		this.plugin = plugin;

		// Get required objects from the provided plugin object
		codeMgr = plugin.getCodeManager();
		helper = plugin.getCommandHelper();

		// Register all the /ccode sub commands
		helper.registerSubCommand("generate <command>",
				"Generates a code for the given command");
		helper.registerSubCommand("remove <code>",
				"Remove the given command code");
		helper.registerSubCommand("redeem <code>",
				"Redeem / activate the given code");
		helper.registerSubCommand("view [pageNo]",
				"See current command codes on the given page");
		helper.registerSubCommand("previous [pageNo]",
				"See previous command codes on the given page");
		helper.registerSubCommand("show <code>",
				"Shows details for the given code");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd,
			final String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("ccode")) { // Literally the only time this method should be called
			if (args.length == 0) {
				helper.sendCommandHelp(sender);
			} else {
				final String sub = args[0].toLowerCase();

				if (sub.equals("generate") || sub.equals("create")) {
					if (!(sender.hasPermission("commandcodes.generate") || sender instanceof ConsoleCommandSender)) {
						sender.sendMessage(ChatColor.DARK_RED
								+ "You don't have permission to do that!");
					} else {
						if (args.length < 3) {
							sender.sendMessage(ChatColor.DARK_RED
									+ "Invalid syntax, /ccode generate <amount> <command>");
						} else {
							boolean ia = false; // Invalid amount
							int amount = -1;

							try {
								amount = Integer.parseInt(args[1]);
							} catch (NumberFormatException e) {
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
				} else if (sub.equals("remove") || sub.equals("delete")) {
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
							} catch (NumberFormatException e) {
								code = 10000000;
								sender.sendMessage(ChatColor.DARK_RED
										+ "Invalid code entered, /ccode remove <code>");
							}

							if (code != 10000000) {
								final CommandCode cc = codeMgr
										.getCurrentCommandCode(code);

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
				} else if (sub.equals("redeem") || sub.equals("activate")) {
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
								} catch (NumberFormatException e) {
									code = 10000000;
									sender.sendMessage(ChatColor.DARK_RED
											+ "Invalid code entered, /ccode redeem <code>");
								}

								final Player player = (Player) sender;

								if (code != 10000000) {
									// Redeems the code with the CodeManager
									final CommandCode cc = codeMgr.redeemed(
											player.getUniqueId(), code);

									if (cc == null) { // If they have already redeemed it
										sender.sendMessage(ChatColor.DARK_RED
												+ "Couldn't redeem command code!");
									} else {
										// Dispatch the command as if player was OP
										boolean cur = player.isOp();
										player.setOp(true);
										plugin.getServer().dispatchCommand(
												player, cc.getCommand());
										player.setOp(cur);
										sender.sendMessage(ChatColor.GRAY
												+ "Redeemed code!");
									}
								}
							}

						}
					}
				} else if (sub.equals("view") || sub.equals("see")) {
					if (!(sender.hasPermission("commandcodes.view") || sender instanceof ConsoleCommandSender)) {
						sender.sendMessage(ChatColor.DARK_RED
								+ "You don't have permission to do that!");
					} else {
						return display(sender, codeMgr.getAvailableCodes(),
								args);
					}
				} else if (sub.equals("previous") || sub.equals("seeprev")) {
					if (!(sender.hasPermission("commandcodes.previous") || sender instanceof ConsoleCommandSender)) {
						sender.sendMessage(ChatColor.DARK_RED
								+ "You don't have permission to do that!");
					} else {
						return display(sender, codeMgr.getPreviousCodes(), args);
					}
				} else if (sub.equals("show") || sub.equals("info")) {
					if (!(sender.hasPermission("commandcodes.view") || sender instanceof ConsoleCommandSender)) {
						sender.sendMessage(ChatColor.DARK_RED
								+ "You don't have permission to do that!");
					} else {
						if (args.length == 1) {
							sender.sendMessage(ChatColor.DARK_RED
									+ "Invalid syntax, /ccode show <code>");
						} else {
							int code;
							try {
								code = Integer.parseInt(args[1]);
							} catch (NumberFormatException e) {
								sender.sendMessage(ChatColor.DARK_RED
										+ "Invalid code number, /ccode show <code>");
								code = 1000000;
							}

							if (code != 1000000) {
								CommandCode cc = codeMgr
										.getCurrentCommandCode(code);
								if (cc == null) {
									cc = codeMgr.getSpentCommandCode(code);
								}

								if (cc == null) {
									sender.sendMessage(ChatColor.DARK_RED
											+ "That code doesn't exist!");
								} else {
									showCommandCodeData(sender, cc);
								}
							}
						}
					}
				}
			}
		}

		return true;
	}

	private boolean display(final CommandSender sender,
			final List<CommandCode> list, final String[] args) {
		final int amount = list.size();
		final int pages = (int) Math.ceil(amount / CODES_PER_PAGE);

		int pageNo = 1;
		if (args.length > 1) {
			try {
				pageNo = Integer.parseInt(args[1]);
			} catch (final NumberFormatException e) {
				sender.sendMessage(ChatColor.DARK_RED + "Invalid page number: "
						+ args[1]);
				return true;
			}

			if (pageNo > pages) {
				sender.sendMessage(ChatColor.DARK_RED
						+ "There aren't that many pages!");
				return true;
			}
		}

		sender.sendMessage(ChatColor.GRAY + "[Codes"
				+ (pageNo > 1 ? " " + pageNo + "/" + pages : "") + "]");

		final int start = CODES_PER_PAGE * (pageNo - 1);
		final int end = start + 7;

		for (int cur = start; cur < end; cur++) {
			if (list.size() > cur) {
				final CommandCode cc = list.get(cur);

				sender.sendMessage(ChatColor.GOLD.toString() + cur + ": Code="
						+ cc.getCode() + ", Command=" + cc.getCommand());
			}
		}

		return true;
	}

	private boolean showCommandCodeData(final CommandSender sender,
			final CommandCode cc) {
		sender.sendMessage(ChatColor.GOLD + "Code: " + cc.getCode());
		sender.sendMessage(ChatColor.GOLD + "Command: " + cc.getCommand());
		sender.sendMessage(ChatColor.GOLD + "Uses: " + cc.getAmount());
		sender.sendMessage(ChatColor.GOLD + "Remaining: "
				+ (cc.getAmount() - cc.getRedeemers().size()));
		sender.sendMessage(ChatColor.GOLD + "Redeemers: "
				+ getPlayersStr(cc.getRedeemers()));

		return true;
	}

	private String getPlayersStr(final List<UUID> uuids) {
		final StringBuilder builder = new StringBuilder();
		for (final UUID currentUuid : uuids) {
			final OfflinePlayer player = plugin.getServer().getOfflinePlayer(
					currentUuid);
			builder.append(player.getName()).append(" ");
		}
		builder.setLength(builder.length() - 1);
		return builder.toString();
	}

	/**
	 * The amount of codes to show on each page
	 */
	private static final int CODES_PER_PAGE = 6;
}
