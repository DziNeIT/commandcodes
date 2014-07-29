package pw.ollie.commandcodes.command.ccode;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import pw.ollie.commandcodes.CommandCodes;
import pw.ollie.commandcodes.code.CodeManager;
import pw.ollie.commandcodes.code.CommandCode;
import pw.ollie.commandcodes.command.CCodeSubCommand;

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
		if (args.length < 4) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "Invalid syntax, /ccode generate <amount> <timesUsable> <command>");
		} else {
			boolean invalid = false;
			int amount = -1;
			int timesUsable = -1;

			try {
				amount = Integer.parseInt(args[1]);
				timesUsable = Integer.parseInt(args[2]);
			} catch (final NumberFormatException e) {
				invalid = true;
			}

			if (amount < 1 || timesUsable < 1) {
				invalid = true;
			}

			if (invalid) {
				sender.sendMessage(ChatColor.DARK_RED
						+ "Invalid arguments (amount and times usable must be at least 1!)");
			} else {
				final StringBuilder builder = new StringBuilder();
				for (int cur = 3; cur < args.length; cur++) {
					builder.append(args[cur]).append(" ");
				}
				builder.setLength(builder.length() - 1);
				final String command = builder.toString();

				final CommandCode[] codes = new CommandCode[amount];
				for (int i = 0; i < codes.length; i++) {
					codes[i] = codeMgr.generateCode(command, timesUsable);
				}

				sender.sendMessage(ChatColor.GOLD
						+ "Codes created for command: '" + command + "'");
				for (final CommandCode code : codes) {
					sender.sendMessage(ChatColor.GRAY + "");
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
