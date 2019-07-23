package com.nu.domain;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import com.nu.command.CreateUserAccountCommand;
import com.nu.command.CreditMoneyCommand;
import com.nu.command.DebitMoneyCommand;
import com.nu.event.MoneyCreditedEvent;
import com.nu.event.MoneyDebitedEvent;
import com.nu.event.UserAccountCreatedEvent;
import com.nu.exception.TransactionException;
import com.nu.exception.UserAccountException;

@Aggregate
public class UserAccountAggregate {

	@AggregateIdentifier
	private String username;

	private BigDecimal balance;

	private Long version;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	public UserAccountAggregate() {
	}
	
	@CommandHandler
    public UserAccountAggregate(CreateUserAccountCommand createUserAccountCommand){
		if (BigDecimal.valueOf(0).compareTo(createUserAccountCommand.getInitialBalance()) > 0) {
			throw new UserAccountException(
					"The initial balance of account must be greater than or equal to zero");
		}
		
		AggregateLifecycle.apply(new UserAccountCreatedEvent(createUserAccountCommand.getId(), createUserAccountCommand.getInitialBalance()));
    }
	
	@CommandHandler
	protected void on(CreditMoneyCommand creditMoneyCommand) {
		AggregateLifecycle.apply(new MoneyCreditedEvent(creditMoneyCommand.getId(), creditMoneyCommand.getCreditAmount()));
	}

	@CommandHandler
	protected void on(DebitMoneyCommand debitMoneyCommand) {
		if (debitMoneyCommand.getDebitAmount().compareTo(this.balance) > 0) {
			throw new TransactionException("There are not enough funds to carry out this operation.");
		}
		
		AggregateLifecycle.apply(new MoneyDebitedEvent(debitMoneyCommand.getId(), debitMoneyCommand.getDebitAmount()));
	}

	@EventSourcingHandler
	protected void on(UserAccountCreatedEvent userAccountCreatedEvent) {
		this.username = userAccountCreatedEvent.getId();
		this.balance = userAccountCreatedEvent.getInitialAmount();
	}
	
	@EventSourcingHandler
	protected void on(MoneyCreditedEvent moneyCreditedEvent) {
		this.balance = this.balance.add(moneyCreditedEvent.getCreditAmount());
	}

	@EventSourcingHandler
	protected void on(MoneyDebitedEvent moneyDebitedEvent) {
		this.balance = this.balance.subtract(moneyDebitedEvent.getDebitAmount());
	}

}
