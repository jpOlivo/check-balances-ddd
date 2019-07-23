package com.nu.event;

import java.math.BigDecimal;

public class MoneyDebitedEvent extends BaseEvent<String> {

	private final BigDecimal debitAmount;

	public MoneyDebitedEvent(String id, BigDecimal debitAmount) {
		super(id);
		this.debitAmount = debitAmount;
	}

	public BigDecimal getDebitAmount() {
		return debitAmount;
	}

}
