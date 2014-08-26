package pw.ollie.commandcodes.command.ccode;

import pw.ollie.commandcodes.CommandCodes;
import pw.ollie.commandcodes.code.CodeManager;
import pw.ollie.commandcodes.code.CommandCode;
import pw.ollie.commandcodes.command.CCodeSubCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED
                    + "Only players can redeem command codes!");
        } else {
            if (args.length == 1) {
                sender.sendMessage(ChatColor.DARK_RED
                        + "Invalid syntax, /ccode redeem <code>");
            } else {
                final String code = args[1];
                final Player player = (Player) sender;

                // Redeems the code with the CodeManager
                final CommandCode cc = codeMgr.redeemCode(player.getUniqueId(), code);
                if (cc == null) { // If they have already redeemed it
                    sender.sendMessage(ChatColor.DARK_RED + "Couldn't redeem command code!");
                } else {
                    sender.sendMessage(ChatColor.GRAY + "Redeemed code!");
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPermission() {
        return "commandcodes.redeem";
    }
}
