package com.github.dzineit.commandcodes.code;

public final class CommandCode {
	private final int code;
	private final String command;

	private String redeemer;

	public CommandCode(final int code, final String command) {
		this.code = code;
		this.command = command;
	}

	public CommandCode(final int code, final String command,
			final String redeemer) {
		this(code, command);

		this.redeemer = redeemer;
	}

	public int getCode() {
		return code;
	}

	public String getCommand() {
		return command;
	}

	public String getRedeemer() {
		return redeemer;
	}

	public void setRedeemer(String redeemer) {
		this.redeemer = redeemer;
	}
}
