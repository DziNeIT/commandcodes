package com.github.dzineit.commandcodes.code;

import java.util.List;

import com.github.dzineit.commandcodes.Util;
import com.github.dzineit.commandcodes.storage.org.json.JSONObject;

/**
 * Represents a single command code, which possesses a code, a command, an
 * amount, and a list of redeemers if it has been redeemed
 */
public final class CommandCode {
	private final int code;
	private final String command;
	private final int amount;

	private List<String> redeemers;

	public CommandCode(final int code, final String command, final int amount) {
		this.code = code;
		this.command = command;
		this.amount = amount;

		redeemers = null;
	}

	public CommandCode(final int code, final String command, final int amount,
			final List<String> redeemers) {
		this(code, command, amount);

		this.redeemers = redeemers;
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

	public List<String> getRedeemers() {
		return redeemers;
	}

	public void setRedeemers(final List<String> redeemers) {
		this.redeemers = redeemers;
	}

	public void addRedeemer(final String redeemer) {
		redeemers.add(redeemer);
	}

	public boolean isUsed() {
		return redeemers.size() >= amount;
	}

	public JSONObject toJSONObject() {
		final JSONObject json = new JSONObject();
		json.put("code", code);
		json.put("command", command);
		json.put("amount", amount);

		if (redeemers != null) {
			json.put("redeemers", Util.listToString(redeemers, "::"));
		}

		return json;
	}

	public static CommandCode fromJSONObject(final JSONObject json) {
		final int code = json.getInt("code");
		final String command = json.getString("command");
		final int amount = json.getInt("amount");

		if (json.has("redeemers")) {
			return new CommandCode(code, command, amount, Util.stringToList(
					json.getString("redeemers"), "::"));
		} else {
			return new CommandCode(code, command, amount);
		}
	}
}
