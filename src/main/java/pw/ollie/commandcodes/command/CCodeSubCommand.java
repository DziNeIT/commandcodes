package pw.ollie.commandcodes.command;

import pw.ollie.commandcodes.CommandCodes;

import org.bukkit.command.CommandSender;

/**
 * Represents a subcommand of the 'ccode' command
 */
public abstract class CCodeSubCommand {
    protected final CommandCodes plugin;

    public CCodeSubCommand(final CommandCodes plugin) {
        this.plugin = plugin;
    }

    /**
     * Executes this subcommand of the 'ccode' command, using the given sender
     * and the given arguments. The first element in the args array will always
     * be the name of the command as entered by the user (i.e it could be any of
     * the names in the array returned by getNames())
     * 
     * @param sender
     *            The CommandSender who sent the command
     * @param args
     *            The arguments provided by the command sender when they typed
     *            the command
     */
    public abstract void execute(final CommandSender sender, final String[] args);

    /**
     * Gets an array of aliases for this subcommand of 'ccode'
     * 
     * @return An array of different aliases for this subcommand
     */
    public abstract String[] getNames();

    /**
     * Gets a string representing the usage of this subcommand, used for sending
     * to a command sender if they request help, for example '/ccode generate
     * <amount> <command>'
     * 
     * @return A usage string for this subcommand
     */
    public abstract String getUsage();

    /**
     * Gets a brief description of this subcommand and what it does, suitable
     * for sending to a command sender in the chat box
     * 
     * @return A brief description of this command's functionality
     */
    public abstract String getDescription();

    /**
     * Gets the permission required to execute this subcommand of 'ccode'
     * 
     * @return The permission required for this command
     */
    public abstract String getPermission();
}
