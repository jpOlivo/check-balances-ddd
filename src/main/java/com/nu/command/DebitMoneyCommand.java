package com.nu.command;

import java.math.BigDecimal;

public class DebitMoneyCommand extends BaseCommand<String> {
	private final BigDecimal debitAmount;

	public DebitMoneyCommand(String id, BigDecimal debitAmount) {
		super(id);
		this.debitAmount = debitAmount;
	}

	public BigDecimal getDebitAmount() {
		return debitAmount;
	}


}
