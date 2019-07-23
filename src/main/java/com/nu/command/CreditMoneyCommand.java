package com.nu.command;

import java.math.BigDecimal;

public class CreditMoneyCommand extends BaseCommand<String> {
	private final BigDecimal creditAmount;

	public CreditMoneyCommand(String id, BigDecimal creditAmount) {
		super(id);
		this.creditAmount = creditAmount;
	}

	public BigDecimal getCreditAmount() {
		return creditAmount;
	}

}
