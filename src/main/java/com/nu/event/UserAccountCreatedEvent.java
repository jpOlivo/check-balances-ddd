package com.nu.event;

import java.math.BigDecimal;

public class UserAccountCreatedEvent extends BaseEvent<String> {
	private final BigDecimal initialAmount;

	public UserAccountCreatedEvent(String id, BigDecimal initialAmount) {
		super(id);
		this.initialAmount = initialAmount;
	}

	public BigDecimal getInitialAmount() {
		return initialAmount;
	}

}
