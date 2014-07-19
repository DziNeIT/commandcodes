package com.github.dzineit.commandcodes.code;

import java.util.List;

public final class CommandCode {
	private final int code;
	private final String command;
	private final int amount;

	private List<String> redeemers;

	public CommandCode(final int code, final String command, final int amount) {
		this.code = code;
		this.command = command;
		this.amount = amount;
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

	public boolean isUsed() {
		return redeemers.size() >= amount;
	}
}
