package com.nu.command;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class BaseCommand<T> {

	@TargetAggregateIdentifier
	private final T id;

	public BaseCommand(T id) {
		this.id = id;
	}

	public T getId() {
		return id;
	}
}
