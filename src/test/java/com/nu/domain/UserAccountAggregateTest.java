package com.nu.domain;

import java.math.BigDecimal;

import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.eventsourcing.eventstore.EventStoreException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nu.command.CreateUserAccountCommand;
import com.nu.command.CreditMoneyCommand;
import com.nu.command.DebitMoneyCommand;
import com.nu.domain.UserAccountAggregate;
import com.nu.event.MoneyCreditedEvent;
import com.nu.event.MoneyDebitedEvent;
import com.nu.event.UserAccountCreatedEvent;
import com.nu.exception.TransactionException;
import com.nu.exception.UserAccountException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAccountAggregateTest {

	private FixtureConfiguration<UserAccountAggregate> fixture;

	@Before
	public void setUp() {
		fixture = new AggregateTestFixture<UserAccountAggregate>(UserAccountAggregate.class);
	}

	@Test
	public void createUserAccount() {
		BigDecimal initialBalance = BigDecimal.valueOf(0);
		fixture.given().when(new CreateUserAccountCommand("juan", initialBalance)).expectSuccessfulHandlerExecution()
				.expectEvents(new UserAccountCreatedEvent("juan", initialBalance));
	}

	@Test
	public void createUserAccountInvalidBalance() {
		fixture.given().when(new CreateUserAccountCommand("juan", BigDecimal.valueOf(-10)))
				.expectException(UserAccountException.class);
	}

	@Test
	public void createExistingUserAccount() {
		fixture.given(new UserAccountCreatedEvent("juan", BigDecimal.valueOf(0)))
				.when(new CreateUserAccountCommand("juan", BigDecimal.valueOf(0)))
				.expectException(EventStoreException.class);
	}

	@Test
	public void depositMoney() {
		fixture.given(new UserAccountCreatedEvent("juan", BigDecimal.valueOf(0)))
				.when(new CreditMoneyCommand("juan", BigDecimal.valueOf(100))).expectSuccessfulHandlerExecution()
				.expectEvents(new MoneyCreditedEvent("juan", BigDecimal.valueOf(100)));
	}

	@Test
	public void depositMoneyOnInexistentAccount() {
		fixture.given().when(new CreditMoneyCommand("juan", BigDecimal.valueOf(0)))
				.expectException(AggregateNotFoundException.class);
	}

	@Test
	public void withdrawMoney() {
		fixture.given(new UserAccountCreatedEvent("juan", BigDecimal.valueOf(100)))
				.when(new DebitMoneyCommand("juan", BigDecimal.valueOf(50))).expectSuccessfulHandlerExecution()
				.expectEvents(new MoneyDebitedEvent("juan", BigDecimal.valueOf(50)));
	}

	@Test
	public void withdrawMoneyOnInexistentAccount() {
		fixture.given()
				.when(new DebitMoneyCommand("fausto", BigDecimal.valueOf(20)))
				.expectException(AggregateNotFoundException.class);
	}

	@Test
	public void overdrawAccount() {
		fixture.given(new UserAccountCreatedEvent("juan", BigDecimal.valueOf(0)))
				.when(new DebitMoneyCommand("juan", BigDecimal.valueOf(50)))
				.expectException(TransactionException.class);
	}

}
