package com.github.dzineit.commandcodes.util;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.github.dzineit.commandcodes.code.CommandCode;

public final class CommandUtil {
	/**
	 * The amount of codes to show on each page
	 */
	private static final int CODES_PER_PAGE = 6;

	public static void displayCodeList(final CommandSender sender,
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
				return;
			}

			if (pageNo > pages) {
				sender.sendMessage(ChatColor.DARK_RED
						+ "There aren't that many pages!");
				return;
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
	}

	public static String getPlayersStr(final List<UUID> uuids) {
		final StringBuilder builder = new StringBuilder();
		for (final UUID currentUuid : uuids) {
			final OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(
					currentUuid);
			builder.append(player.getName()).append(" ");
		}
		builder.setLength(builder.length() - 1);
		return builder.toString();
	}

	private CommandUtil() {
	}
}
