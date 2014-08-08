package pw.ollie.commandcodes.command.ccode;

import pw.ollie.commandcodes.CommandCodes;
import pw.ollie.commandcodes.code.CodeManager;
import pw.ollie.commandcodes.command.CCodeSubCommand;
import pw.ollie.commandcodes.util.CommandUtil;

import org.bukkit.command.CommandSender;

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
        CommandUtil.displayCodeList(sender, codeMgr.getAvailableCodes(), args);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPermission() {
        return "commandcodes.view";
    }
}
