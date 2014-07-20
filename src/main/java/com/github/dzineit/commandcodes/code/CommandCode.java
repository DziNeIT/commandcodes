package com.github.dzineit.commandcodes.code;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.github.dzineit.commandcodes.storage.org.json.JSONObject;
import com.github.dzineit.commandcodes.util.GeneralUtil;

/**
 * Represents a single command code, which possesses a code, a command, an
 * amount, and a list of redeemers if it has been redeemed
 */
public final class CommandCode {
	/**
	 * The integer code which represents this command code
	 */
	private final int code;
	/**
	 * The command executed when this command code is used
	 */
	private final String command;
	/**
	 * The amount of times this code is redeemable
	 */
	private final int amount;

	/**
	 * Whether the command code is spent (this is true if it isn't a currently
	 * available code)
	 */
	private boolean spent;
	/**
	 * A list of people who have redeemed this code
	 */
	private List<UUID> redeemers;

	public CommandCode(final int code, final String command, final int amount) {
		this.code = code;
		this.command = command;
		this.amount = amount;

		redeemers = new ArrayList<>();
		spent = false;
	}

	public CommandCode(final int code, final String command, final int amount,
			final List<UUID> redeemers, final boolean spent) {
		this(code, command, amount);

		this.redeemers = redeemers;
		this.spent = spent;
	}

	public int getCode() {
		return code;
	}

	public String getCommand() {
		return command;
	}

	public int getAmount() {
		return amount;
	}

	public List<UUID> getRedeemers() {
		return redeemers;
	}

	public void setRedeemers(final List<UUID> redeemers) {
		this.redeemers = redeemers;
	}

	public void addRedeemer(final UUID redeemer) {
		redeemers.add(redeemer);

		if (amount - redeemers.size() <= 0) {
			spent = true;
		}
	}

	public boolean isSpent() {
		return spent;
	}

	/**
	 * Transforms this CommandCode into a JSONObject for storage
	 * 
	 * @return A JSONObject containing this CommandCode's data
	 */
	public JSONObject toJSONObject() {
		final JSONObject json = new JSONObject();
		json.put("code", code);
		json.put("command", command);
		json.put("amount", amount);
		json.put("spent", spent);

		if (redeemers != null) {
			json.put("redeemers", GeneralUtil.uuidListToString(redeemers, "::"));
		}

		return json;
	}

	/**
	 * Creates a CommandCode object using the data in the given JSONObject
	 * 
	 * @param json
	 *            The JSONObject which contains data for the new CommandCode
	 * @return A CommandCode object who's data is the same as that stored in the
	 *         JSONObject
	 */
	public static CommandCode fromJSONObject(final JSONObject json) {
		final int code = json.getInt("code");
		final String command = json.getString("command");
		final int amount = json.getInt("amount");
		final boolean spent = json.getBoolean("spent");

		if (json.has("redeemers")) {
			return new CommandCode(code, command, amount,
					GeneralUtil.uuidStringToList(json.getString("redeemers"),
							"::"), spent);
		} else {
			return new CommandCode(code, command, amount);
		}
	}
}
