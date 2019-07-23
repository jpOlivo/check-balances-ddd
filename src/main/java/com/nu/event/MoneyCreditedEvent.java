package com.nu.event;

import java.math.BigDecimal;

public class MoneyCreditedEvent extends BaseEvent<String> {
	private final BigDecimal creditAmount;

	public MoneyCreditedEvent(String id, BigDecimal creditAmount) {
		super(id);
		this.creditAmount = creditAmount;
	}

	public BigDecimal getCreditAmount() {
		return creditAmount;
	}

}
