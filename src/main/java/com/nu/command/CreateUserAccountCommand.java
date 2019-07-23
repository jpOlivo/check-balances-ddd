package com.nu.command;

import java.math.BigDecimal;

public class CreateUserAccountCommand extends BaseCommand<String> {
	private final BigDecimal initialBalance;

	public CreateUserAccountCommand(String id, BigDecimal initialBalance) {
		super(id);
		this.initialBalance = initialBalance;
	}

	public BigDecimal getInitialBalance() {
		return initialBalance;
	}

}
